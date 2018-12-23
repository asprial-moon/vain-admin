package com.vain.quartz.task;

import com.alibaba.fastjson.JSON;
import com.vain.constant.SystemConfigKeys;
import com.vain.dao.IRedisDao;
import com.vain.entity.OperationLog;
import com.vain.log.constant.LogConstants;
import com.vain.service.IOperationLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.OperationType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

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
            operationLogService.addBatch(logs.stream().map(logString -> {
                OperationLog operationLog = JSON.parseObject(logString, OperationLog.class);
                if (null == operationLog.getUserId()) {
                    operationLog.setUserId(0);
                }
                if (null == operationLog.getClassName()) {
                    operationLog.setClassName("");
                }
                if (null == operationLog.getMethodName()) {
                    operationLog.setMethodName("");
                }
                if (null == operationLog.getOperationType()) {
                    operationLog.setOperationType(LogConstants.OperationLogType.OPERATION_DEFAULT);
                }
                if (null == operationLog.getOperationData()) {
                    operationLog.setOperationData("");
                }
                if (null == operationLog.getOperationIP()) {
                    operationLog.setOperationIP("");
                }
                if (null == operationLog.getInfo()) {
                    operationLog.setInfo("");
                }
                return operationLog;
            }).collect(Collectors.toList()));
            log.info("写入日志{}条", logs.size());
            redisDao.trim(SystemConfigKeys.OPERATION_LOG_KEY, logs.size(), -1);
        }
    }
}
