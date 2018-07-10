package com.vain.component;

import com.jcraft.jsch.*;
import com.vain.constant.SystemConfigKeys;
import com.vain.entity.UploadFile;
import com.vain.enums.UploadFileType;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author vain
 * @Description
 * @date 2018/6/19 22:23
 */
@Component(value = "uploaderFileComponent")
@Slf4j
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class SFTPUploadFile extends UploaderFileComponent {

    private Session session;

    private ChannelSftp channelSftp;

    @Override
    public void init() {
        try {
            JSch jsch = new JSch();
            session = jsch.getSession(
                    sysConfigComponent.getStringValue(SystemConfigKeys.SYS_FILE_UPLOAD_HOST_USER),
                    sysConfigComponent.getStringValue(SystemConfigKeys.SYS_FILE_UPLOAD_HOST_IP),
                    sysConfigComponent.getIntValue(SystemConfigKeys.SYS_FILE_UPLOAD_HOST_PORT));
            session.setPassword(sysConfigComponent.getStringValue(SystemConfigKeys.SYS_FILE_UPLOAD_HOST_PASSWD));
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            channelSftp = (ChannelSftp) session.openChannel("sftp");
            channelSftp.connect();
        } catch (JSchException e) {
            e.printStackTrace();
            log.error("sftp init error {}", e.getMessage());
        }
    }

    @Override
    @PreDestroy
    public void destroy() {
        if (channelSftp != null && channelSftp.isConnected()) {
            channelSftp.disconnect();
        }
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    @Override
    public UploadFile upload(InputStream inputStream, UploadFile file, Integer needResize) {
        if (null == inputStream) {
            return null;
        }
        if (null == file) {
            file = new UploadFile(UploadFileType.OTHER.getType());
        }
        if (file.getType().equals(UploadFileType.PHOTO.getType())) {
            inputStream = super.resize(inputStream, file, needResize, 0.8f);
        }
        //文件路径
        file.setPath(combineUrl(file.getUuid(), sysConfigComponent.getStringValue(SystemConfigKeys.SYS_FILE_UPLOAD_ROOT_PATH), file.getType().toString()));
        if (StringUtils.isNotEmpty(file.getName()) && file.getName().contains(".")) {
            file.setPath(file.getPath() + file.getName().substring(file.getName().lastIndexOf(".")));
        }
        try {
            initDir(file.getPath().substring(0, file.getPath().lastIndexOf("/")));
            channelSftp.put(inputStream, file.getPath());
            // 上传成功后，设置url
            file.setUrl(combineUrl(file.getUuid(), sysConfigComponent.getStringValue(SystemConfigKeys.SYS_FILE_UPLOAD_ROOT_URL), file.getType().toString()));
            if (StringUtils.isNotEmpty(file.getName()) && file.getName().contains(".")) {
                file.setUrl(file.getUrl() + file.getName().substring(file.getName().lastIndexOf(".")));
            }
            file.setLength((long) inputStream.available());
        } catch (Exception e) {
            log.error("sftp upload file error: {}", e.getMessage());
        }
        return file;
    }

    private void initDir(String dir) throws Exception {
        try {
            channelSftp.stat(dir);
            log.debug("dir " + dir + " already exists");
        } catch (SftpException e) {
            // 目录不存在，需要逐级创建
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                String[] tmps = dir.split("/");
                String subDir = "";
                for (String tmp : tmps) {
                    if (tmp.length() == 0) {
                        continue;
                    }

                    subDir += "/" + tmp;

                    try {
                        channelSftp.stat(subDir);
                    } catch (SftpException e1) {
                        // 目录不存在，从前往后依次创建
                        if (e1.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                            channelSftp.mkdir(subDir);
                            log.debug("make dir " + subDir);
                        } else {
                            log.error("make dir error: {}", e1.getMessage());
                            throw e1;
                        }
                    }

                }
            } else {
                throw e;
            }
        }
    }

}
