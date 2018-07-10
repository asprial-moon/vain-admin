package com.vain.shiro.realm;

import com.vain.constant.SystemConfigKeys;
import com.vain.entity.Menu;
import com.vain.service.IMenuService;
import com.vain.shiro.authenticator.SubjectInfo;
import com.vain.shiro.token.Token;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.AllPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by vain on 2017/9/19.
 * 实现自己的shiro认证登录方式
 */
@Setter
@Slf4j
public class DefaultAuthorizingRealm extends AuthorizingRealm {

    @Autowired
    private IMenuService menuService;

    List<AuthenticateRealm> authenticateRealms;

    /**
     * 构造传入token否则会爆出 UnsupportedTokenException
     *
     * @see org.apache.shiro.authc.pam.UnsupportedTokenException
     */
    public DefaultAuthorizingRealm() {
        super();
        this.setAuthenticationTokenClass(Token.class);
    }

    /**
     * 用户权限分为： 角色权限和菜单权限
     * 可以在SimpleAuthorizationInfo 设置角色信息 添加角色的判断
     *
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        log.info("获取授权信息");
        if (principals == null) {
            log.error("密码为空");
            throw new AuthenticationException("PrincipalCollection method argument cannot be null.");
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        SubjectInfo subjectInfo = (SubjectInfo) session.getAttribute(SystemConfigKeys.USER_KEY);
        int userType = subjectInfo == null ? -1 : subjectInfo.getUserType();
        if (userType == SystemConfigKeys.SUPER_ADMIN) {
            info.addObjectPermission(new AllPermission());
        } else {
            if (null != subjectInfo) {
                HashSet<Menu> menus = menuService.getMenusByUserId(subjectInfo.getUserId(), subjectInfo.getUserType());
                Set<String> permissions = new HashSet<>();
                menus.forEach(m -> permissions.add(m.getMenuKey()));
                info.setStringPermissions(permissions);
            }
        }
        return info;
    }

    /**
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        log.info("获取认证信息");
        SubjectInfo subjectInfo = authenticate((Token) token);
        if (subjectInfo == null) {
            log.error("用户的认证信息为空");
            throw new AuthenticationException("SubjectInfo is null");
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute(SystemConfigKeys.USER_KEY, subjectInfo);
        AuthenticationInfo info = new SimpleAuthenticationInfo(subjectInfo.getIdentification(), token.getCredentials(), getName());
        return info;
    }

    private SubjectInfo authenticate(Token token) throws AuthenticationException {
        SubjectInfo subjectInfo = null;
        if (authenticateRealms == null) {
            log.error("realm为空请检查配置");
            throw new AuthenticationException();
        }
        //依次验证配置的realm
        for (AuthenticateRealm authenticateRealm : authenticateRealms) {
            if (authenticateRealm.accept(token)) {
                subjectInfo = authenticateRealm.login(token);
                break;
            }
        }
        return subjectInfo;
    }

}
