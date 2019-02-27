package com.vain.shiro.filter;

import com.vain.entity.Menu;
import com.vain.service.IMenuService;
import com.vain.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterConfig;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vain on 2017/9/20.
 * filterManager管理
 * 初始化时候读取菜单列表 并根据对应的url设置mapValue
 */
@Slf4j
@Setter
@Getter
public class PermissionFilterManager extends DefaultFilterChainManager {

    @Autowired
    private IMenuService menuService;

    /**
     * 重试次数
     */
    private AtomicInteger tryCounts = new AtomicInteger(0);

    private static final Integer MAX_TRY_COUNTS = 5;

    /**
     * shiro 配置文件中获取filter名称
     */
    private String filterName;

    public PermissionFilterManager() {
    }

    public PermissionFilterManager(FilterConfig filterConfig) {
        super(filterConfig);
    }

    public void init() {

        try {
            List<Menu> menus = menuService.getList(null, false);
            Map<String, String> urlPerms = new HashMap<>(menus.size());
            for (Menu data : menus) {
                if (!StringUtils.isEmpty(data.getUrl())) {
                    //将url 作为key  menuKey作为权限value
                    urlPerms.put(data.getUrl(), data.getMenuKey());
                }
            }
            if (null == this.filterName) {
                this.filterName = "perms";
            }
            // 将mapValue 设置到对应的filterName中
            for (Map.Entry<String, String> entry : urlPerms.entrySet()) {
                this.addToChain(entry.getKey(), filterName, entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            log.error("PermissionFilterManager init fail,try again!");
            if (tryCounts.get() < MAX_TRY_COUNTS) {
                tryCounts.incrementAndGet();
                init();
            } else {
                log.error("PermissionFilterManager init fail,Try to fail five times,Please check your code!");
            }
        }
    }
}
