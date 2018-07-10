package com.vain.service;

import com.vain.base.service.BaseService;
import com.vain.entity.ScheduleJob;
import org.quartz.SchedulerException;

public interface IScheduleJobService extends BaseService<ScheduleJob> {

    /**
     * 触发一次任务
     *
     * @param job
     */
    void triggerJob(ScheduleJob job) throws SchedulerException;

    /**
     * 重新开始任务
     *
     * @param job
     */
    void resumeJob(ScheduleJob job) throws SchedulerException;

    /**
     * 暂停任务
     *
     * @param job
     */
    void pauseJob(ScheduleJob job) throws SchedulerException;
}
