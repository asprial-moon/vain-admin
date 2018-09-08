package com.vain.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author vain
 * @Description
 * @date 2018/9/8 15:39
 */
public class HttpContext {
    private final static String X_FORWARDED_FOR = "x-forwarded-for";
    private final static String UNKNOWN = "unknown";
    private final static String PROXY_CLIENT_IP = "Proxy-Client-IP";
    private final static String WL_PROXY_CLIENT_IP = "WL-Proxy-Client-IP";
    private final static String X_REQUEST = "X-Requested-With";
    private final static String XML_HTTP_REQUEST = "XMLHttpRequest";

    public final static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public final static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public final static HttpSession getSession() {
        return HttpContext.getRequest().getSession();
    }

    public final static void setSessionAttritube(String name, Object value) {
        HttpContext.getSession().setAttribute(name, value);
    }

    public final static Object getSessionAttritube(String name) {
        return HttpContext.getSession().getAttribute(name);
    }

    public final static void removeAttritube(String name) {
        HttpContext.getSession().removeAttribute(name);
    }

    public final static String getRemoteAddress() {
        HttpServletRequest request = HttpContext.getRequest();
        String ip = request.getHeader(X_FORWARDED_FOR);
        if (ip != null && (ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip))) {
            ip = request.getHeader(PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader(WL_PROXY_CLIENT_IP);
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public final static boolean isAjax() {
        String requestType = getRequest().getHeader(X_REQUEST);
        return requestType != null && requestType.equals(XML_HTTP_REQUEST);
    }

}