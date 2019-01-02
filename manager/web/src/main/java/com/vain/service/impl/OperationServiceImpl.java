package com.vain.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.component.ElasticsearchComponent;
import com.vain.constant.ElasticsearchConstant;
import com.vain.entity.ElasticsearchPageEntity;
import com.vain.entity.OperationLog;
import com.vain.mapper.OperationLogMapper;
import com.vain.service.IOperationLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OperationServiceImpl extends AbstractBaseService implements IOperationLogService {

    @Resource
    private OperationLogMapper operationLogMapper;

    @Resource
    private ElasticsearchComponent elasticsearchComponent;

    @Override
    public PageInfo<OperationLog> getPagedList(OperationLog entity) throws ErrorCodeException {
        entity.initPageParam();
        Map<String, Object> matchString = new HashMap<>(2);
        if (null != entity.getOperationType()) {
            matchString.put("operationType", entity.getOperationType());
        }
        matchString.put("operationData", null == entity.getOperationData() ? "" : entity.getOperationData());
        ElasticsearchPageEntity operationData = elasticsearchComponent.page(ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE, 0, 0, null, null,
                false, "operationData", matchString, null, entity.getCurrentPage(), entity.getPageSize());
        if (null != operationData) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setTotal(operationData.getTotal());
            List<Map<String, Object>> datas = operationData.getData();
            List<OperationLog> logs = Lists.newArrayList();
            for (Map<String, Object> data : datas) {
                String s = JSON.toJSONString(data);
                logs.add(JSONObject.parseObject(s, OperationLog.class));
            }
            pageInfo.setList(logs);
            pageInfo.setPageSize(operationData.getPageSize());
            pageInfo.setPageNum(operationData.getCurrentPage());
            return pageInfo;
        }
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
            if (null != entity.getId()) {
                elasticsearchComponent.delete(ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE, entity.getId() + "");
            }
            if (!CollectionUtils.isEmpty(entity.getIds())) {
                for (Integer id : entity.getIds()) {
                    elasticsearchComponent.delete(ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE, id + "");
                }
            }
            return operationLogMapper.delete(entity);
        }
        return 0;
    }

    @Override
    public int addBatch(List<OperationLog> operationLogList) {
        if (CollectionUtils.isEmpty(operationLogList)) {
            return 0;
        }
        int i = operationLogMapper.insertBatch(operationLogList);
        elasticsearchComponent.addBatch(operationLogList.stream().map(operationLog -> JSONObject.parseObject(JSONObject.toJSONString(operationLog))).collect(Collectors.toList()),
                ElasticsearchConstant.LOGS_INDEX, ElasticsearchConstant.LOGS_TYPE);
        return i;
    }
}
