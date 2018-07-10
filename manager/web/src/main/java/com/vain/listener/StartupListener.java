package com.vain.listener;

import com.vain.component.SysConfigComponent;
import com.vain.component.TaskComponent;
import com.vain.component.UploaderFileComponent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author vain
 * @description: 系统启动时读取组件信息
 * @date 2017/8/31 12:00
 */
@Component
@Slf4j
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private SysConfigComponent sysConfigComponent;

    @Autowired
    private TaskComponent taskComponent;

    @Autowired
    private UploaderFileComponent uploaderFileComponent;

    @Override
    public synchronized void onApplicationEvent(ContextRefreshedEvent event) {
        // 需要执行的逻辑代码，当spring容器初始化完成后就会执行该方法。
        log.info("startup completed");

        // 此处初始化数据库里保存的配置项
        sysConfigComponent.loadSystemConfigFromDb();

        // 此处初始化数据库定时任务
        taskComponent.loadTaskFromDb();

        // 此处初始化上传文件链接
        uploaderFileComponent.init();
    }
}