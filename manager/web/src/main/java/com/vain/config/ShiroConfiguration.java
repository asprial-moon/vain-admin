package com.vain.config;

import com.vain.shiro.filter.PermissionFilterManager;
import com.vain.shiro.realm.AuthenticateRealm;
import com.vain.shiro.realm.DefaultAuthorizingRealm;
import com.vain.shiro.realm.SimpleAuthenticateRealm;
import com.vain.shiro.web.WebShiroFilterFactoryBean;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @
 */
@Configuration
public class ShiroConfiguration {

    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(SecurityManager securityManager, PermissionFilterManager permissionFilterManager) {
        WebShiroFilterFactoryBean shiroFilterFactoryBean = new WebShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        shiroFilterFactoryBean.setFilterChainManager(permissionFilterManager);
        return shiroFilterFactoryBean;
    }

    @Bean
    public PermissionFilterManager getDefaultFilterChainManager() {
        //动态加载权限列表
        PermissionFilterManager permissionFilterManager = new PermissionFilterManager();
        permissionFilterManager.setFilterName("user");
        return permissionFilterManager;
    }

    @Bean
    public SecurityManager getSecurityManager(DefaultAuthorizingRealm realm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        // 禁用session
        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
        securityManager.setSubjectDAO(subjectDAO);
        return securityManager;
    }

    @Bean
    public SimpleAuthenticateRealm simpleAuthenticateRealm() {
        return new SimpleAuthenticateRealm();
    }

    @Bean
    public DefaultAuthorizingRealm getRealm(SimpleAuthenticateRealm simpleAuthenticateRealm) {
        DefaultAuthorizingRealm realm = new DefaultAuthorizingRealm();
        List<AuthenticateRealm> myRealms = new ArrayList<>();
        myRealms.add(simpleAuthenticateRealm);
        realm.setAuthenticateRealms(myRealms);
        return realm;
    }
}
