package com.vain.entity;


import com.vain.base.entity.PagedEntity;
import lombok.*;

import java.sql.Timestamp;

/**
 * @author vain
 * @date： 2017/10/11 15:04
 * @description： t_role_menu对应的实体类
 */
@Setter
@Getter
public class RoleMenu extends PagedEntity {

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 菜单id
     */
    private Integer menuId;

    private Timestamp createTime;

    private Timestamp modifyTime;

    public RoleMenu(Integer roleId) {
        this.roleId = roleId;
    }

    public RoleMenu(Integer roleId, Integer menuId) {
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
