package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import com.vain.util.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * @author vain
 * @date 2017/11/7 21:23
 * @description 上传文件实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFile extends PagedEntity {
    /**
     * 生成UUID
     */
    private String uuid;

    /**
     * 源名
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 文件长度
     */
    private Long length;

    /**
     * 文件上传用户
     */
    private Integer userId;

    /**
     * 上传访问路径
     */
    private String url;

    /**
     * 服务器地址
     */
    private String path;

    /**
     * 图片宽度
     */
    private Integer width;

    /**
     * 图片高度
     */
    private Integer height;


    private Timestamp createTime;

    private Timestamp modifyTime;

    public UploadFile(String UUID, String name, Integer type, Long length, Integer userId) {
        this.name = name;
        this.type = type;
        this.length = length;
        this.userId = userId;
        this.uuid = java.util.UUID.randomUUID().toString();
        this.path = DateUtils.dateToStr(Calendar.getInstance().getTime(), "yyyy-MM-dd") + "/" + type + "/" + java.util.UUID.randomUUID().toString();
    }

    public UploadFile(Integer type) {
        this.type = type;
        this.uuid = java.util.UUID.randomUUID().toString();
        this.path = DateUtils.dateToStr(Calendar.getInstance().getTime(), "yyyy-MM-dd") + "/" + type + "/" + java.util.UUID.randomUUID().toString();
    }
}
