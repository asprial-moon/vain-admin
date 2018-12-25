package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

/**
 * @author vain
 * @description: 系统组件配置实体
 * @date 2017/8/31 11:57
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SystemConfig extends PagedEntity {

    /**
     * 配置项名字
     */
    private String name;

    /**
     * 配置项key
     */
    private String code;

    /**
     * 配置项值，如果值类型是String[]，使用英文逗号隔开
     */
    private String value;

    /**
     * 配置项值类型 1：String 2：INT 3：String[]
     */
    private Integer valueType;

    /**
     * 配置项类型 1：系统基础配置 2：鉴权配置 3：短信配置 4：推送配置 5：三方系统配置 6：爬虫配置
     */
    private Integer type;

    /**
     * 配置项描述
     */
    private String description;

    /**
     * 配置项是否可见
     */
    private Integer visible;

    /**
     * 配置项创建时间
     */
    private Timestamp createTime;

    /**
     * 配置项最后一次修改时间
     */
    private Timestamp modifyTime;

}
