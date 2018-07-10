package com.vain.shiro.token;

import lombok.Data;

/**
 * Created by vain on 2017/9/21.
 * 自己token信息 扩展自己的属性
 */
@Data
public class AccountToken implements Token {
    /**
     * 登录账号
     */
    private String userName;

    /**
     * 登录密码
     */
    private String password;

    /**
     * 用户类型
     */
    private int type;

    /**
     * 是否记住
     */
    private boolean rememberMe;

    public AccountToken(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    /**
     * @return 实现自己的token 在返回主要凭据的时候返回的是用户名
     */
    @Override
    public Object getPrincipal() {
        return this.userName;
    }

    /**
     * @return 实现自己的token 在返回主要凭据的时候返回的是密码
     */
    @Override
    public Object getCredentials() {
        return this.password;
    }
}
