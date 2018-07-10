package com.vain.shiro.realm;

import com.vain.common.ErrorCodeException;
import com.vain.entity.User;
import com.vain.service.IUserService;
import com.vain.shiro.authenticator.SubjectInfo;
import com.vain.shiro.authenticator.UserSubjectInfo;
import com.vain.shiro.exception.AuthenticationException;
import com.vain.shiro.token.AccountToken;
import com.vain.shiro.token.Token;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by vain on 2017/9/19.
 * 简单认证 只认证账号密码 然后获取信息
 */
@Service
@Slf4j
public class SimpleAuthenticateRealm implements AuthenticateRealm {

    @Autowired
    private IUserService userService;

    @Override
    public SubjectInfo login(Token token) {
        UserSubjectInfo userSubjectInfo = null;
        String principal = (String) token.getPrincipal();
        String password = (String) token.getCredentials();
        User user = new User();
        if (StringUtils.isNumeric(principal)) {
            user.setPhone(principal);
        } else if (StringUtils.isEmail(principal)) {
            user.setEmail(principal);
        } else {
            user.setUserName(principal);
        }
        user.setPassword(password);
        User dbUser;
        try {
            dbUser = userService.login(user);
            if (dbUser != null) {
                userSubjectInfo = new UserSubjectInfo();
                // 账号Id
                userSubjectInfo.setUserId(dbUser.getId());
                // 登录名
                userSubjectInfo.setUserName(dbUser.getUserName());
                userSubjectInfo.setNickName(dbUser.getNickname());
                userSubjectInfo.setUserType(dbUser.getType());
            }
        } catch (ErrorCodeException e) {
            log.error("login failure", e);
            throw new AuthenticationException(String.valueOf(e.getCode()), e.getMessage());
        }
        return userSubjectInfo;
    }

    @Override
    public boolean accept(Token token) {
        return token instanceof AccountToken;
    }
}
