package com.vain.component;

import com.aliyun.oss.OSSClient;
import com.vain.constant.SystemConfigKeys;
import com.vain.entity.UploadFile;
import com.vain.enums.UploadFileType;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author vain
 * @Description 阿里云oss
 * @date 2018/6/19 21:21
 */
@Slf4j
public class OSSUploadFile extends UploaderFileComponent {

    private OSSClient ossClient;

    @Override
    public void init() {
        //获取数据库配置 创建oss客户端
        ossClient = new OSSClient(sysConfigComponent.getStringValue(SystemConfigKeys.OSS_ENDPOINT),
                sysConfigComponent.getStringValue(SystemConfigKeys.OSS_ACCESS_KEYID),
                sysConfigComponent.getStringValue(SystemConfigKeys.OSS_ACCESSKEY_SECRET));
    }

    @Override
    public void destroy() {
        if (null != ossClient) {
            ossClient.shutdown();
        }
    }

    @Override
    public UploadFile upload(InputStream inputStream, UploadFile file, Integer needResize) {
        if (null == inputStream || null == ossClient) {
            return null;
        }
        if (null == file) {
            file = new UploadFile(UploadFileType.OTHER.getType());
        }
        if (file.getType().equals(UploadFileType.PHOTO.getType())) {
            inputStream = super.resize(inputStream, file, needResize, 0.8f);
        }
        try {
            //设置文件大小
            file.setLength((long) inputStream.available());
            //上传文件
            ossClient.putObject(sysConfigComponent.getStringValue(SystemConfigKeys.OSS_BUCKETNAME),
                    (file.getUuid()), inputStream);
            file.setUrl(this.combineUrl(file.getUuid(), sysConfigComponent.getStringValue(SystemConfigKeys.OSS_FILE_URL), file.getType().toString()));
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            log.error("upload file error {}", e.getMessage());
        }
        return null;
    }
}
