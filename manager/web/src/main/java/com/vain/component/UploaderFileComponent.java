package com.vain.component;

import com.alibaba.simpleimage.ImageRender;
import com.alibaba.simpleimage.SimpleImageException;
import com.alibaba.simpleimage.render.ReadRender;
import com.alibaba.simpleimage.render.ScaleParameter;
import com.alibaba.simpleimage.render.ScaleRender;
import com.alibaba.simpleimage.render.WriteRender;
import com.vain.entity.UploadFile;
import com.vain.enums.UploadFileType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * @author vain
 * @Description
 * @date 2018/6/19 21:20
 */

@Slf4j
@Data
public abstract class UploaderFileComponent {

    @Autowired
    protected SysConfigComponent sysConfigComponent;

    /**
     * 压缩图片
     *
     * @param inputStream
     */
    protected InputStream resize(InputStream inputStream, UploadFile uploadFile, Integer resizeThreshold, float resizePercentage) {
        if (null == inputStream || null == uploadFile || uploadFile.getType() != UploadFileType.PHOTO.getType() || null == resizeThreshold || 0 >= resizeThreshold) {
            return inputStream;
        }
        WriteRender wr = null;
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            // 获取图片的宽高1
            BufferedImage read = ImageIO.read(inputStream);
            uploadFile.setWidth(read.getWidth());
            uploadFile.setHeight(read.getHeight());
            if (inputStream.available() >= resizeThreshold) {
                //将图像分辨缩略
                ScaleParameter scaleParam = new ScaleParameter(read.getWidth(), read.getHeight());
                ImageRender rr = new ReadRender(inputStream);
                ImageRender sr = new ScaleRender(rr, scaleParam);
                wr = new WriteRender(sr, outStream);
                //触发图像处理
                wr.render();
                Thumbnails.of(inputStream).scale(1f).outputQuality(inputStream.available() > resizeThreshold ? resizePercentage : 1f).toOutputStream(outStream);
            }
            return new ByteArrayInputStream(outStream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("read upload file error: {}", e.getMessage());
        } finally {
            if (null != wr) {
                try {
                    wr.dispose();
                } catch (SimpleImageException e) {
                    e.printStackTrace();
                    log.error("close writeRender error: {}", e.getMessage());
                }
            }
        }
        return inputStream;
    }


    /**
     * 上传图片逻辑
     *
     * @param inputStream
     * @param file
     * @param needResize
     * @return
     */
    public abstract UploadFile upload(InputStream inputStream, UploadFile file, Integer needResize);


    /**
     * 初始化操作
     * 连接信息在Bean创建完成之后才会从数据库读取文件
     * 所以采用bean的 init的方法初始化连接的时候
     * 会显示 读取的数据配置为空
     */
    public abstract void init();

    /**
     * 销毁
     */
    public abstract void destroy();

    /**
     * 拼接文件路径
     *
     * @param prefix 前缀
     * @param suffix 后缀
     * @return
     */
    protected String combineUrl(String suffix, String... prefix) {
        StringBuilder stringBuilder = new StringBuilder();
        for (String s : prefix) {
            stringBuilder.append(s).append("/");
        }
        return stringBuilder.append(suffix).toString();
    }


}
