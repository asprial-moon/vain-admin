package com.vain.utils;


import com.vain.entity.ScheduleJob;
import com.vain.util.SpringUtils;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author vain
 * @date： 2017/10/31 11:23
 * @description 通过反射调用任务job执行方法 通过IOC容器获得的对象 里面注入的bean 不会为空
 */
@Slf4j
public class TaskUtils {

    /**
     * 通过反射调用scheduleJob中定义的方法
     *
     * @param scheduleJob
     */
    public static void invokeMethod(ScheduleJob scheduleJob) {
        Object instance = null;
        Class clazz = null;
        if (!StringUtils.isBlank(scheduleJob.getSpringName())) {
            instance = SpringUtils.getBean(scheduleJob.getSpringName());
        } else if (!StringUtils.isBlank(scheduleJob.getJobClass())) {
            try {
                clazz = Class.forName(scheduleJob.getJobClass());
                instance = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (Field field : fields) {
                    if (field.isAnnotationPresent(Autowired.class)) {
                        //通过反射获取的对象 需要手动注入bean对象
                        Class<?> aClass = Class.forName(field.getType().getName());
                        Object operationServiceImpl = SpringUtils.getBean(aClass);
                        field.setAccessible(true);
                        field.set(instance, operationServiceImpl);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (instance == null) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]未启动成功，请检查是否配置正确");
            return;
        }
        Method method = null;
        try {
            method = instance.getClass().getDeclaredMethod(scheduleJob.getJobMethod());
        } catch (NoSuchMethodException e) {
            log.error("任务名称 = [" + scheduleJob.getJobName() + "]未启动成功，方法名设置错误");
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        if (method != null) {
            try {
                method.invoke(instance);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]启动成功");
    }
}
