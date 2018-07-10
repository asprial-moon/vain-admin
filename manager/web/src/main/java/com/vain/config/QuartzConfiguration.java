package com.vain.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @author vain
 * @Description
 * @date 2018/6/9 14:03
 */
@Configuration
public class QuartzConfiguration {
    @Bean
    public SchedulerFactoryBean getScheduleFactoryBean() {
        return new SchedulerFactoryBean();
    }


}
