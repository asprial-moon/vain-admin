package com.vain.service.impl;

import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.ScheduleJob;
import com.vain.enums.DeletedStatus;
import com.vain.enums.ScheduleStatus;
import com.vain.enums.StatusCode;
import com.vain.mapper.ScheduleJobMapper;
import com.vain.quartz.QuartzJobFactory;
import com.vain.quartz.QuartzJobFactoryDisallowConcurrent;
import com.vain.service.IScheduleJobService;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author vain
 * @date： 2017/10/31 11:53
 * @description：JobKey.jobKey() 根据任务名称来获取 所以如果修改了任务名称 会导致找不到
 */
@Service
@Slf4j
public class ScheduleJobServiceImpl extends AbstractBaseService implements IScheduleJobService {

    @Resource
    private SchedulerFactoryBean schedulerFactoryBean;

    @Resource
    private ScheduleJobMapper scheduleMapper;

    @Override
    public PageInfo<ScheduleJob> getPagedList(ScheduleJob entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public List<ScheduleJob> getList(ScheduleJob entity) throws ErrorCodeException {
        return scheduleMapper.getList(entity);
    }

    @Override
    public ScheduleJob get(ScheduleJob entity) throws ErrorCodeException {
        return scheduleMapper.get(entity);
    }

    @Override
    public int add(ScheduleJob entity) throws ErrorCodeException {
        //状态为开启的才添加
        if (entity == null || ScheduleStatus.RUN.getState() != entity.getJobStatus()) {
            return 0;
        }
        try {
            addJob(entity);
        } catch (SchedulerException e) {
            log.error("任务名称 = [" + entity.getJobName() + "] 添加失败 " + e.getMessage());
            throwErrorCodeException(StatusCode.SCHEDULE_EXPRESSION_ERROR);
        }
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int modify(ScheduleJob entity) throws ErrorCodeException {
        //cron表达式有修改
        if (!StringUtils.isBlank(entity.getCronExpression())) {
            try {
                //在运行状态才去修改表达式
                if (ScheduleStatus.RUN.getState() == entity.getJobStatus()) {
                    updateJobCronExpression(entity);
                } else {
                    //否则只修改数据库
                    //触发一次任务并验证 防止输入错误的cron表达式
                    triggerJob(entity);
                }
            } catch (SchedulerException e) {
                log.error("任务名称 = [" + entity.getJobName() + "] 修改失败 " + e.getMessage());
                throwErrorCodeException(StatusCode.SCHEDULE_EXPRESSION_ERROR);
            }
        }
        return scheduleMapper.update(entity);
    }

    @Override
    public int delete(ScheduleJob entity) throws ErrorCodeException {
        try {
            deleteJob(entity);
            entity.setDeleted(DeletedStatus.STATUS_DELETED.getStatus());
        } catch (SchedulerException e) {
            log.error("任务名称 = [" + entity.getJobName() + "] 删除失败 " + e.getMessage());
            return 0;
        }
        return scheduleMapper.delete(entity);
    }

    /**
     * 添加任务
     *
     * @param job
     */
    private void addJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if (null == trigger) {
            //任务不存在 注册一个
            Class clazz = job.getIsConcurrent() == (ScheduleStatus.CONCURRENT.getState()) ? QuartzJobFactory.class : QuartzJobFactoryDisallowConcurrent.class;
            JobDetail jobDetail = JobBuilder.newJob(clazz).withIdentity(job.getJobName(), job.getJobGroup()).build();
            jobDetail.getJobDataMap().put("scheduleJob", job);
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(cronScheduleBuilder).build();
            //添加任务详情 触发器
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("任务名称 = [" + job.getJobName() + "] 新建成功");
        } else {
            //存在 仅需更新设置
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
            trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(cronScheduleBuilder).build();
            scheduler.rescheduleJob(triggerKey, trigger);
            log.info("任务名称 = [" + job.getJobName() + "] 修改成功");
        }
    }

    /**
     * 暂时任务
     *
     * @param job
     * @throws SchedulerException
     */
    @Override
    public void pauseJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复任务
     *
     * @param job
     * @throws SchedulerException
     */
    @Override
    public void resumeJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除任务
     *
     * @param job
     * @throws SchedulerException
     */
    private void deleteJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.deleteJob(jobKey);
    }

    /**
     * 触发执行一次任务
     *
     * @param job
     * @throws SchedulerException
     */
    @Override
    public void triggerJob(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        JobKey jobKey = JobKey.jobKey(job.getJobName(), job.getJobGroup());
        scheduler.triggerJob(jobKey);
    }

    /**
     * 更新cron表达式
     *
     * @param job
     * @throws SchedulerException
     */
    private void updateJobCronExpression(ScheduleJob job) throws SchedulerException {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, trigger);
    }
}
