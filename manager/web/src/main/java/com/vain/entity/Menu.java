package com.vain.entity;


import com.vain.base.entity.PagedEntity;
import lombok.*;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by vain on 2017/9/23.
 * 用户菜单权限
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Menu extends PagedEntity {

    /**
     * 菜单名称
     */
    private String name;

    /**
     * 父菜单ID
     */
    private Integer parentId;

    /**
     * 菜单的key
     */
    private String menuKey;

    /**
     * 类型 1：目录  2：菜单  3：按钮
     */
    private Integer type;

    /**
     * 访问路径
     */
    private String url;

    /**
     * 图标
     */
    private String icon;

    /**
     * 菜单描述
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
     * 所属用户id
     */
    private Integer userId;

    /**
     * 用户角色id
     */
    private Integer roleId;

    /**
     * 是否拥有子菜单
     */
    private Boolean hasChildren;

    /**
     * 是否拥有菜单的权限
     */
    private Boolean hasPermission;

    /**
     * 子菜单
     */
    private List<Menu> children;

    public Menu(String menuKey) {
        this.menuKey = menuKey;
    }

    public Menu(Integer userId) {
        this.userId = userId;
    }
}
