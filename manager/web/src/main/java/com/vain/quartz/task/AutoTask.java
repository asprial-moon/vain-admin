package com.vain.quartz.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component(value = "autoTask")
public class AutoTask {

    public void autoTaskTest() {
        log.info("定时任务测试");
    }
}
