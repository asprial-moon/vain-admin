package com.vain.controller;

import com.alibaba.fastjson.JSON;
import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.component.UploaderFileComponent;
import com.vain.entity.UploadFile;
import com.vain.enums.UploadFileType;
import com.vain.mapper.UploadFileMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vain
 * @date： 2017/11/8 9:34
 * @description： 文件上传
 */
@Slf4j
@RequestMapping(value = "/upload")
@RestController
public class FileUploadController extends AbstractBaseController<UploadFile> {

    @Resource
    private UploaderFileComponent uploaderFileComponent;

    @Resource
    private UploadFileMapper uploadFileMapper;

    /**
     * 图片上传接口
     */
    @RequestMapping(value = "/uploadPics", method = RequestMethod.POST)
    public void uploadPics(@RequestParam("file") MultipartFile[] files, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Response<UploadFile> res = new Response<>();
        List<UploadFile> pics = new ArrayList<>();
        Integer currentUserId = getCurrentUserId(request);
        for (MultipartFile file : files) {
            UploadFile uploadFile = new UploadFile(UploadFileType.PHOTO.getType());
            uploadFile.setName(file.getOriginalFilename());
            uploadFile.setLength(file.getSize());
            uploadFile.setUserId(1);
            uploadFile = uploaderFileComponent.upload(file.getInputStream(), uploadFile, -1);
            uploadFile.setUserId(currentUserId);
            uploadFileMapper.insert(uploadFile);
            pics.add(uploadFile);
        }
        res.setData(pics);
        // 解决IE9弹下载框的问题
        response.setContentType("text/html");
        // 解决返回参数乱码
        response.setCharacterEncoding("UTF-8");

        String respBody = JSON.toJSONString(res);
        log.info(respBody);

        PrintWriter pw = response.getWriter();
        pw.write(respBody);
        pw.close();
    }

    @RequestMapping(value = "/uploadAudio", method = RequestMethod.POST)
    public void uploadAudio(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFile uploadFile = new UploadFile(UploadFileType.AUDIO.getType());
        uploaderFileComponent.upload(file.getInputStream(), uploadFile, -1);
    }

    @RequestMapping(value = "/uploadVideo", method = RequestMethod.POST)
    public void uploadVideo(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UploadFile uploadFile = new UploadFile(UploadFileType.OTHER.getType());
        uploaderFileComponent.upload(file.getInputStream(), uploadFile, -1);
    }

}
