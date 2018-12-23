package com.vain.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.OperationLog;
import com.vain.mapper.OperationLogMapper;
import com.vain.service.IOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;


@Service
public class OperationServiceImpl extends AbstractBaseService implements IOperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Override
    public PageInfo<OperationLog> getPagedList(OperationLog entity) throws ErrorCodeException {
        entity.initPageParam();
        PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize());
        return new PageInfo<>(operationLogMapper.getList(entity));
    }

    @Override
    public List<OperationLog> getList(OperationLog entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public OperationLog get(OperationLog entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public OperationLog findById(Integer id) throws ErrorCodeException {
        return null;
    }

    @Override
    public int add(OperationLog entity) throws ErrorCodeException {
        return operationLogMapper.insert(entity);
    }

    @Override
    public int modify(OperationLog entity) throws ErrorCodeException {
        return 0;
    }

    @Override
    public int delete(OperationLog entity) throws ErrorCodeException {
        if (entity != null && (!CollectionUtils.isEmpty(entity.getIds()) || entity.getId() != null)) {
            return operationLogMapper.delete(entity);
        }
        return 0;
    }

    @Override
    public int addBatch(List<OperationLog> operationLogList) {
        if (CollectionUtils.isEmpty(operationLogList)) {
            return 0;
        }
        return operationLogMapper.insertBatch(operationLogList);
    }
}
