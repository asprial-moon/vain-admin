package com.vain.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.SystemConfig;
import com.vain.mapper.SystemConfigMapper;
import com.vain.service.ISystemConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author vain
 * @date 2017/10/14 21:51
 * @description 系统配置接口
 */
@Service
public class SystemConfigServiceImpl extends AbstractBaseService implements ISystemConfigService {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Override
    public PageInfo<SystemConfig> getPagedList(SystemConfig entity) throws ErrorCodeException {
        entity.initPageParam();
        PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize());
        return new PageInfo<>(systemConfigMapper.getList(entity));
    }

    @Override
    public List<SystemConfig> getList(SystemConfig entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public SystemConfig get(SystemConfig entity) throws ErrorCodeException {
        return systemConfigMapper.get(entity);
    }

    @Override
    public SystemConfig findById(Integer id) throws ErrorCodeException {
        return null;
    }

    @Override
    public int add(SystemConfig entity) throws ErrorCodeException {
        return 0;
    }

    @Override
    public int modify(SystemConfig entity) throws ErrorCodeException {
        return systemConfigMapper.update(entity);
    }

    @Override
    public int delete(SystemConfig entity) throws ErrorCodeException {
        return 0;
    }
}
