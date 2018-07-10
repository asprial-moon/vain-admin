package com.vain.entity;


import com.vain.base.entity.PagedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * @author vain
 * @date： 2017/10/31 11:40
 * @description： 定时任务实体类 t_schedule_job
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleJob extends PagedEntity {

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组
     */
    private String jobGroup;

    /**
     * 任务状态
     */
    private Integer jobStatus;

    /**
     * cron表达式
     */
    private String cronExpression;

    /**
     * 任务描述
     */
    private String description;

    /**
     * 任务执行类（全路径）
     */
    private String jobClass;

    /**
     * 任务执行类执行方法
     */
    private String jobMethod;

    /**
     * 是否同步
     */
    private Integer isConcurrent;

    /**
     * spring容器中的对象名称（优先级大于类的全路径）
     */
    private String springName;

    private int deleted;

    private Timestamp createTime;

    private Timestamp modifyTime;
}

