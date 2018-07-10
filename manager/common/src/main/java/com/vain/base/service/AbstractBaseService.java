package com.vain.base.service;


import com.vain.common.ErrorCodeException;
import com.vain.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author vain
 * @description: sevice类的抽象类
 * @date 2017/8/31 11:49
 */
@Slf4j
public abstract class AbstractBaseService {


    /**
     * throwErrorCodeException:对于错误的返回码，需要调用此方法，抛出异常给上层处理
     *
     * @param code
     * @throws ErrorCodeException
     */
    protected void throwErrorCodeException(int code, String message) throws ErrorCodeException {
        throw new ErrorCodeException(code, message);
    }

    protected void throwErrorCodeException(StatusCode statusCode) throws ErrorCodeException {
        throw new ErrorCodeException(statusCode.getCode(), statusCode.getMessage());
    }

}
