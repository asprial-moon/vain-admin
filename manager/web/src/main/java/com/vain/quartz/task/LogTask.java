package com.vain.quartz.task;

import com.alibaba.fastjson.JSON;
import com.vain.constant.SystemConfigKeys;
import com.vain.dao.IRedisDao;
import com.vain.entity.OperationLog;
import com.vain.service.IOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author vain
 * @Description
 * @date 2018/6/18 17:29
 */

@Slf4j
@Component(value = "logTask")
public class LogTask {

    @Autowired
    private IOperationLogService operationLogService;

    @Autowired
    private IRedisDao redisDao;

    public synchronized void saveLogs() {
        List<String> logs = redisDao.getList(SystemConfigKeys.OPERATION_LOG_KEY, 0, -1);
        if (!CollectionUtils.isEmpty(logs)) {
            logs.forEach(logString -> {
                OperationLog operationLog = JSON.parseObject(logString, OperationLog.class);
                operationLogService.add(operationLog);
            });
            log.info("写入日志{}条", logs.size());
            redisDao.trim(SystemConfigKeys.OPERATION_LOG_KEY,  logs.size(),-1);
        }
    }
}
