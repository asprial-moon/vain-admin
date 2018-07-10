package com.vain.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.vain.base.entity.Response;
import com.vain.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by vain on 2017/9/20.
 * referer拦截
 */
@Slf4j
public class UrlFilter extends UserFilter {
    /**
     * UserFilter 当前用户必须登录或者是通过之前的登录时的remember me可以获得principalCollection，也就是必须知道用户是谁才可以。否则返回false
     * 通过获取请求的referer来保证 除了shiro配置了anno的路径或文件 其余的都需要保证是项目内部跳转 并且通过UserFilter的验证
     * 只拦截了页面跳转
     *
     * @param request
     * @param response
     * @param mappedValue
     * @return
     * @see org.apache.shiro.web.filter.authc.UserFilter
     * @see org.apache.shiro.web.filter.authc.FormAuthenticationFilter
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        String referer = WebUtils.toHttp(request).getHeader("Referer");
        log.info("referer = " + referer);
        //暂不做处理
        return true;
    }

    /**
     * 返回403码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse res = WebUtils.toHttp(response);
        res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    /**
     * 从定向登录页面
     *
     * @param servletRequest
     * @param servletResponse
     * @throws IOException
     */
    @Override
    protected void redirectToLogin(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        response.setCharacterEncoding("utf-8");
        response.setHeader("content-Type", "application/json");
        PrintWriter writer = response.getWriter();
        log.error("url filter redirect ");
        writer.write(JSON.toJSONString(new Response(StatusCode.ACCOUNT_LOGIN)));
        writer.close();
    }
}
