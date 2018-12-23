package com.vain.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.vain.base.entity.Response;
import com.vain.dao.IRedisDao;
import com.vain.entity.Menu;
import com.vain.entity.User;
import com.vain.enums.StatusCode;
import com.vain.service.IMenuService;
import com.vain.util.SpringUtils;
import com.vain.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;

/**
 * @author vain
 * @date： 2017/10/27 11:39
 * @description： 基于t-menu的menuKey来拦截(数据库配置的路径基于web配置的api之后 否则mapperValue会为空!!)
 */
@Slf4j
public class UserFilter extends AuthorizationFilter {

    @Resource
    private IMenuService menuService;

    @Resource
    private IRedisDao redisDao;

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mapperValue) {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String token = request.getHeader("Token");
        //访问没有带身份(Token)信息 直接不允许
        if (StringUtils.isEmpty(token)) {
            log.error(request.getRequestURI() + " token is null");
            return false;
        }
        if (TokenUtils.isTokenExpired(token)) {
            log.error(request.getRequestURI() + " token is expire");
            return false;
        }
        if (redisDao == null) {
            redisDao = SpringUtils.getBean("redisDaoImpl");
        }
        String[] perms = (String[]) mapperValue;
        if (perms != null && perms.length > 0) {
            if (redisDao.containsKey(token)) {
                //存在缓存 直接判断权限
                String value = redisDao.getValue(token);
                List<String> menuList = JSON.parseArray(value, String.class);
                if (!CollectionUtils.isEmpty(menuList)) {
                    if (menuList.parallelStream().anyMatch(perms[0]::contains)) {
                        return true;
                    }
                }
            } else {
                //redis中没有权限缓存(或 重新要求登录)
                Claims claim = TokenUtils.getClaimFromToken(token);
                if (claim == null) {
                    return false;
                }
                String id = claim.getId();
                User user = new User();
                if (StringUtils.isEmpty(id)) {
                    return false;
                }
                user.setId(Integer.valueOf(id));
                HashSet<Menu> myMenus = menuService.getMenusByUserId(Integer.valueOf(id), 0);
                redisDao.cacheValue(token, JSON.toJSONString(myMenus), TokenUtils.expiration);
                if (myMenus.parallelStream().anyMatch(menu -> perms[0].equals(menu.getMenuKey()))) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String token = request.getHeader("Token");
        response.setCharacterEncoding("utf-8");
        response.setHeader("content-Type", "application/json");
        PrintWriter writer = response.getWriter();
        if (StringUtils.isEmpty(token)) {
            log.error(request.getRequestURI() + " token is null");
            writer.write(JSON.toJSONString(new Response(StatusCode.ACCOUNT_LOGIN)));
            writer.close();
            return false;
        }
        if (TokenUtils.isTokenExpired(token)) {
            log.error(request.getRequestURI() + " token is expire");
            writer.write(JSON.toJSONString(new Response(StatusCode.TOKEN_INVALID)));
            writer.close();
            return false;
        }
        writer.write(JSON.toJSONString(new Response(StatusCode.FORBIDDEN)));
        writer.close();
        return false;
    }
}
