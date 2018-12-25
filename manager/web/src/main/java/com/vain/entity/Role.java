package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by vain on 2017/9/23.
 * 角色信息
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role extends PagedEntity {

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色key
     */
    private String roleKey;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 删除标识符
     */
    private Integer deleted;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp modifyTime;

    /**
     * 角色权限集合列表
     */
    private List<Menu> menus;
}
