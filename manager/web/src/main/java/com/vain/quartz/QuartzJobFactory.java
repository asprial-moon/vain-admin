package com.vain.quartz;

import com.vain.entity.ScheduleJob;
import com.vain.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author vain
 * @date： 2017/10/31 14:53
 * @description： 同步 不管上次是否执行完成
 */
@Slf4j
public class QuartzJobFactory implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        TaskUtils.invokeMethod(scheduleJob);
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]");
    }
}
