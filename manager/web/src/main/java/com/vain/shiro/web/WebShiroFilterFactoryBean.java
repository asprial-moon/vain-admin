package com.vain.shiro.web;

import com.vain.shiro.filter.PermissionFilterManager;
import com.vain.shiro.filter.UrlFilter;
import com.vain.shiro.filter.UserFilter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.util.Nameable;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.FilterChainManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.util.CollectionUtils;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author vain
 * @Description
 * @date 2018/6/2 20:49
 */
@Slf4j
@Setter
@Getter
public class WebShiroFilterFactoryBean extends ShiroFilterFactoryBean {

    /**
     * 设置拦截链 设置filter的mapValue
     *
     * @see org.apache.shiro.web.filter.AccessControlFilter 的mappedValue
     */
    private FilterChainManager filterChainManager;

    public WebShiroFilterFactoryBean() {
        setFilters(new HashMap<>(2));
        setFilterChainDefinitionMap(new LinkedHashMap<>());
    }

    /**
     * 设置自己的Filters
     *
     * @param filters the optional filterName-to-Filter map of filters available for
     *                reference when creating null null null null null
     *                {@link #setFilterChainDefinitionMap (java.util.Map) filter
     */
    @Override
    public void setFilters(Map<String, Filter> filters) {
        filters.put("url", new UrlFilter());
        filters.put("user", new UserFilter());
        super.setFilters(filters);
        log.debug("shiro filter init :" + filters.size());
    }

    /**
     * 设置拦截map
     *
     * @param filterChainDefinitionMap the chainName-to-chainDefinition map of chain definitions to
     *                                 use for creating filter chains intercepted by the Shiro
     */
    @Override
    public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
        filterChainDefinitionMap.put("/css/**", "anon");
        filterChainDefinitionMap.put("/js/**", "anon");
        filterChainDefinitionMap.put("/images/**", "anon");
        filterChainDefinitionMap.put("/resources/**", "anon");
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/user/logout", "anon");
        filterChainDefinitionMap.put("/user/get/", "anon");
        filterChainDefinitionMap.put("/user/modifyPersonInfo", "anon");
        filterChainDefinitionMap.put("/user/modifyPersonPassword", "anon");
        filterChainDefinitionMap.put("/menu/getMyMenus", "anon");
        filterChainDefinitionMap.put("/upload/uploadPics", "anon");
        filterChainDefinitionMap.put("/druid/**", "anon");
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/**", "url,user");
        super.setFilterChainDefinitionMap(filterChainDefinitionMap);
        log.debug("shiro filterChainDefinitionMap init :" + filterChainDefinitionMap.size());
    }

    /**
     * 创建filter拦截 可以扩展AbstractShiroFilter 实现全局Filter前判断
     *
     * @return
     * @throws Exception
     */
    @Override
    protected AbstractShiroFilter createInstance() throws Exception {
        return super.createInstance();
    }


    @Override
    protected FilterChainManager createFilterChainManager() {
        FilterChainManager chainManager = null;
        if (null != this.getFilterChainManager()) {
            chainManager = this.getFilterChainManager();
        } else {
            chainManager = new DefaultFilterChainManager();
        }
        Map<String, Filter> filters = getFilters();
        if (!CollectionUtils.isEmpty(filters)) {
            for (Map.Entry<String, Filter> entry : filters.entrySet()) {
                String name = entry.getKey();
                Filter filter = entry.getValue();
                if (filter instanceof Nameable) {
                    ((Nameable) filter).setName(name);
                }
                chainManager.addFilter(name, filter, false);
            }
        }
        if (chainManager instanceof PermissionFilterManager) {
            if (null == ((PermissionFilterManager) chainManager).getFilterName()) {
                //默认在userFilter判断请求权限
                ((PermissionFilterManager) chainManager).setFilterName("user");
            }
            ((PermissionFilterManager) chainManager).init();
        }

        Map<String, String> chains = getFilterChainDefinitionMap();
        if (!CollectionUtils.isEmpty(chains)) {
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                chainManager.createChain(url, chainDefinition);
            }
        }
        return chainManager;
    }
}
