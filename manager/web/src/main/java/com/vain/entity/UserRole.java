package com.vain.entity;


import com.vain.base.entity.PagedEntity;
import lombok.*;

import java.sql.Timestamp;

/**
 * @author vain
 * @date： 2017/10/21 18:01
 * @description： 用户-角色对应表
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRole extends PagedEntity {

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 角色id
     */
    private Integer roleId;

    private Timestamp createTime;

    private Timestamp modifyTime;

    public UserRole(Integer userId, Integer roleId) {
        this.userId = userId;
        this.roleId = roleId;
    }
}
