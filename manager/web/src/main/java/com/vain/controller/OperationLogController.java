package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.entity.OperationLog;
import com.vain.enums.StatusCode;
import com.vain.service.IOperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author vain
 * @date： 2017/11/6 14:58
 * @description： 操作日志类
 */
@RequestMapping(value = "/log")
@RestController
public class OperationLogController extends AbstractBaseController<OperationLog> {

    @Autowired
    private IOperationLogService operationLogService;

    @PostMapping(value = "/getPagedList")
    public Response<OperationLog> getPagedList(@RequestBody OperationLog operationLog) {
        return new Response<OperationLog>().setData(operationLogService.getPagedList(operationLog));
    }

    @PostMapping(value = "/delete")
    public Response delete(@RequestBody OperationLog operationLog) {
        if (operationLog == null || (operationLog.getIds() == null && operationLog.getId() == null) || (operationLog.getId() != null && operationLog.getIds() != null)) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<OperationLog>().setData(operationLogService.delete(operationLog));
    }
}
