package com.vain.entity;

import com.vain.base.entity.PagedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author vain
 * @description: 用户信息实体类
 * @date 2017/8/31 11:57
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User extends PagedEntity {

    /**
     * 用户类型 1-超级管理员2-管理组3-普通用户
     */
    private int type;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码盐值
     */
    private String salt;
    /**
     * 昵称
     */
    private String nickname;
    /**
     * 电话
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像url
     */
    private String avatar;
    /**
     * 性别1-男2-女
     */
    private int sex;
    /**
     * 生日
     */
    private Timestamp birthday;

    /**
     * 是否锁定0-正常1-锁定
     */
    private int state;
    /**
     * 删除标识位0-正常1-已删除
     */
    private int deleted;
    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp modifyTime;

    /**
     * 新密码
     */
    private String newpasswd;

    /**
     * 用户的角色
     */
    private List<Role> roles;

    /**
     * 批量处理的数据id集合
     */
    private List<Integer> ids;

    /**
     * 拥有权限集合
     */
    private List<Menu> menus;

    /**
     * 清楚敏感字段
     */
    public void clearSecretField() {
        this.password = null;
        this.salt = null;
    }
}
