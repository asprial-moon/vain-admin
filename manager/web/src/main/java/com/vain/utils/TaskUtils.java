package com.vain.utils;


import com.vain.entity.ScheduleJob;
import com.vain.util.SpringUtils;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author vain
 * @date： 2017/10/31 11:23
 * @description 通过反射调用任务job执行方法 通过IOC容器获得的对象 里面注入的bean 不会为空
 * 如果是通过反射获取到的对象  里面的bean为空 注意NullPointException
 */
@Slf4j
public class TaskUtils {

    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param scheduleJob
     */
    public static void invokeMethod(ScheduleJob scheduleJob) {
        Object object = null;
        Class clazz = null;
        if (!StringUtils.isBlank(scheduleJob.getSpringName())) {
            object = SpringUtils.getBean(scheduleJob.getSpringName());
        } else if (!StringUtils.isBlank(scheduleJob.getJobClass())) {
            try {
                clazz = Class.forName(scheduleJob.getJobClass());
                object = clazz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (object == null) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]未启动成功，请检查是否配置正确");
            return;
        }
        Method method = null;
        try {
            method = object.getClass().getDeclaredMethod(scheduleJob.getJobMethod());
        } catch (NoSuchMethodException e) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]未启动成功，方法名设置错误");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (method != null) {
            try {
                method.invoke(object);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]启动成功");
    }
}
