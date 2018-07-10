package com.vain.shiro.realm;


import com.vain.shiro.authenticator.SubjectInfo;
import com.vain.shiro.token.Token;

/**
 * Created by vain on 2017/9/19.
 */
public interface AuthenticateRealm {

    SubjectInfo login(Token token);

    boolean accept(Token token);
}
