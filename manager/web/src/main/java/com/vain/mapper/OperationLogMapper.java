package com.vain.mapper;

import com.vain.entity.OperationLog;

import java.util.List;


public interface OperationLogMapper {

    List<OperationLog> getList(OperationLog operationLog);

    int insert(OperationLog operationLog);

    int delete(OperationLog operationLog);
}
