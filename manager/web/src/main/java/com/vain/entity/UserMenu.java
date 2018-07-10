package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author vain
 * @Descritpion t_user_menu 用户权限对应列表
 * @Date 21:42 2017/10/13
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMenu extends PagedEntity {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 菜单id
     */
    private Integer menuId;

    private Timestamp createTime;

    private Timestamp modifyTime;
}
