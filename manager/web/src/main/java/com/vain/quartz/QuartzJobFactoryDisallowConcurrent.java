package com.vain.quartz;

import com.vain.entity.ScheduleJob;
import com.vain.utils.TaskUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author vain
 * @date 2017/10/29 17:19
 * @description 若一个方法一次执行不完下次轮转时则等待改方法执行完后才执行下一次操作(禁止并发)
 */
@DisallowConcurrentExecution
@Slf4j
public class QuartzJobFactoryDisallowConcurrent implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("任务成功运行");
        ScheduleJob scheduleJob = (ScheduleJob) jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        TaskUtils.invokeMethod(scheduleJob);
        log.info("任务名称 = [" + scheduleJob.getJobName() + "]");
    }
}
