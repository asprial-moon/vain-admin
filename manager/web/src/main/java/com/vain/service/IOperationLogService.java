package com.vain.service;

import com.vain.base.service.BaseService;
import com.vain.entity.OperationLog;

import java.util.List;

public interface IOperationLogService extends BaseService<OperationLog> {

    int addBatch(List<OperationLog> operationLogList);
}
