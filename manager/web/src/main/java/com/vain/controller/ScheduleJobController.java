package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.entity.ScheduleJob;
import com.vain.enums.ScheduleStatus;
import com.vain.enums.StatusCode;
import com.vain.service.IScheduleJobService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vain
 * @date 2017/10/31 21:26
 * @description 定时任务接口
 * 如果在实际中只是单纯的使用定时任务做一些简单的任务
 * 不需要动态及时修改执行表达式的时候，或者执行任务并没有多个执行方法可供选择的的时候
 * 其实可以使用spring自带的task来完成定时
 * <bean id="taskJob" class="com.vain.manager.quartz.AutoFinishJob"/>
 * <task:scheduled-tasks>
 * <task:scheduled ref="taskJob" method="autoTask" cron="0 0 0 * * ?"/>
 * </task:scheduled-tasks>
 * 或者使用@Scheduled
 */
@RequestMapping("/scheduleJob")
@RestController
public class ScheduleJobController extends AbstractBaseController<ScheduleJob> {

    @Autowired
    private IScheduleJobService scheduleJobService;

    @PostMapping(value = "/getList")
    public Response<ScheduleJob> getList(@RequestBody ScheduleJob entity) {
        return new Response<ScheduleJob>().setDataList(scheduleJobService.getList(entity));
    }

    @PostMapping(value = "/get")
    public Response<ScheduleJob> get(@RequestBody ScheduleJob entity) {
        return new Response<ScheduleJob>().setData(scheduleJobService.get(entity));
    }

    /**
     * 修改定时任务（只做了修改表达式）
     * 如果要修改任务名和任务组 可以先移除，添加 ，修改
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modify")
    public Response<ScheduleJob> modify(@RequestBody ScheduleJob entity) {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<ScheduleJob>().setData(scheduleJobService.modify(entity));
    }

    /**
     * 移除定时任务
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public Response<ScheduleJob> delete(@RequestBody ScheduleJob entity) {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<ScheduleJob>().setData(scheduleJobService.delete(entity));
    }

    /**
     * 触发一次定时任务
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/triggerJob")
    public Response triggerJob(@RequestBody ScheduleJob entity) throws SchedulerException {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        scheduleJobService.triggerJob(entity);
        return Response.success();
    }


    /**
     * 重新开启定时任务
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/resumeJob")
    public Response resumeJob(@RequestBody ScheduleJob entity) throws SchedulerException {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        ScheduleJob job = scheduleJobService.get(entity);
        if (job.getJobStatus() != ScheduleStatus.RUN.getState()) {
            //添加到schedule中
            scheduleJobService.add(job);
            job.setCronExpression(null);
            scheduleJobService.modify(job);
        } else {
            //重新开启
            scheduleJobService.resumeJob(entity);
        }
        job.setJobStatus(ScheduleStatus.RUN.getState());
        scheduleJobService.modify(job);
        return Response.success();
    }


    /**
     * 暂停定时任务
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/pauseJob")
    public Response pauseJob(@RequestBody ScheduleJob entity) throws Exception {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        scheduleJobService.pauseJob(entity);
        entity.setJobStatus(ScheduleStatus.NOT_RUN.getState());
        scheduleJobService.modify(entity);
        return Response.success();
    }

}
