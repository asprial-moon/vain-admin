package com.vain.component;

import com.alibaba.fastjson.JSON;
import com.vain.entity.SystemConfig;
import com.vain.mapper.SystemConfigMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ContextLoader;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * @author vain
 * @description: 系统配置组件  在系统启动的时候读取系统一些不常修改的配置信息
 * @date 2017/8/31 11:52
 */
@Component
@Slf4j
public class SysConfigComponent {

    @Resource
    private SystemConfigMapper sysConfigDao;

    private HashMap<String, String> configMapFromDb = new HashMap<>();

    public void loadSystemConfigFromDb() {
        List<SystemConfig> sysConfigs = sysConfigDao.getList(null);
        if (!CollectionUtils.isEmpty(sysConfigs)) {
            sysConfigs.forEach(systemConfig -> configMapFromDb.put(systemConfig.getCode(), systemConfig.getValue()));
        }
        log.info("加载{}项系统配置", sysConfigs == null ? 0 : sysConfigs.size());
    }

    public String getStringValue(String key) {
        return configMapFromDb.get(key);
    }

    public int getIntValue(String key) {
        return Integer.parseInt(getStringValue(key));
    }

    public String[] getSringArrayValue(String key) {
        return getStringValue(key).split("\\,");
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    private static SysConfigComponent instance;

    public static SysConfigComponent instance() {
        if (instance == null) {
            instance = ContextLoader.getCurrentWebApplicationContext().getBean(SysConfigComponent.class);
        }

        return instance;
    }

    /**
     * 在修改后重新加载数据
     */
    public void reload() {
        configMapFromDb = new HashMap<>(10);
        log.info("------------------重新加载系统配置信息------------------");
        loadSystemConfigFromDb();//重新加载
    }

}
