package com.vain.component;

import com.vain.entity.ScheduleJob;
import com.vain.service.IScheduleJobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * @author vain
 * @date： 2017/10/31 11:28
 * @description： 定时任务组件
 */
@Component
@Slf4j
public class TaskComponent {

    @Autowired
    private IScheduleJobService scheduleJobService;

    public void loadTaskFromDb() {
        List<ScheduleJob> list = scheduleJobService.getList(null);
        if (!CollectionUtils.isEmpty(list)) {
            list.forEach(scheduleJob -> scheduleJobService.add(scheduleJob));
        }
        log.info("加载{}项定时任务配置", list == null ? 0 : list.size());
    }
}
