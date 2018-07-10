package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.component.SysConfigComponent;
import com.vain.entity.SystemConfig;
import com.vain.enums.StatusCode;
import com.vain.service.ISystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vain
 * @date 2017/10/14 21:41
 * @description 系统配置 接口
 * 系统配置在项目初始化就已经加载
 * 所以允许修改 清空 重新加载数据
 */
@RequestMapping(value = "/systemConfig")
@RestController
public class SystemConfigController extends AbstractBaseController<SystemConfig> {

    @Autowired
    private ISystemConfigService systemConfigService;

    @Autowired
    private SysConfigComponent sysConfigComponent;

    /**
     * 获取系统配置信息
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getPagedList")
    public Response<SystemConfig> getPagedList(@RequestBody SystemConfig entity) {
        return new Response<SystemConfig>().setData(systemConfigService.getPagedList(entity));
    }

    /**
     * 获取单个系统配置
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/get")
    public Response<SystemConfig> get(@RequestBody SystemConfig entity) {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<SystemConfig>().setData(systemConfigService.get(entity));
    }

    /**
     * 修改系统配置信息
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modify")
    public Response<SystemConfig> modify(@RequestBody SystemConfig entity) {
        sysConfigComponent.reload();
        return new Response<SystemConfig>().setData(systemConfigService.modify(entity));
    }
}
