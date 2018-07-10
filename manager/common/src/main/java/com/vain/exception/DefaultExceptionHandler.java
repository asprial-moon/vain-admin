package com.vain.exception;

import com.vain.base.entity.Entity;
import com.vain.base.entity.Response;
import com.vain.common.ErrorCodeException;
import com.vain.enums.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.time.DateTimeException;

/**
 * @author vain
 * @description: 捕获异常 返回相应的code和msg
 * @date 2017/8/31 11:48
 */
@ControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

    /**
     * 定义全局异常处理，value属性可以过滤拦截条件，此处拦截特定的Exception
     */
    @ExceptionHandler({Exception.class})
    public
    @ResponseBody
    Response<Entity> handleErrorCodeException(Exception e) {
        Response<Entity> response = new Response<>();
        log.error(e.getMessage(), e);
        if (e instanceof ErrorCodeException) {
            response.setCode(((ErrorCodeException) e).getCode());
            response.setMessage(e.getMessage());
        } else {
            log.error(e.getMessage(), e);
            response.setStatusCode(StatusCode.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @ExceptionHandler({HttpMessageNotWritableException.class})
    public
    @ResponseBody
    Response handleJSONConvertException(HttpMessageNotWritableException httpMessageNotWriteAbleException) {
        log.error(httpMessageNotWriteAbleException.getMessage(), httpMessageNotWriteAbleException.getCause());
        return Response.error("数据转换异常");
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public
    @ResponseBody
    Response handleJSONReadException(HttpMessageNotReadableException httpMessageNotReadableException) {
        log.error(httpMessageNotReadableException.getMessage(), httpMessageNotReadableException.getCause());
        return Response.error("数据转换异常");
    }

    @ExceptionHandler({NumberFormatException.class})
    public
    @ResponseBody
    Response handleNumberFormatException(NumberFormatException numberFormatException) {
        log.error(numberFormatException.getMessage(), numberFormatException.getCause());
        return Response.error("数字转换异常");
    }

    @ExceptionHandler({DateTimeException.class})
    public
    @ResponseBody
    Response handleDateTimeException(DateTimeException dateTimeException) {
        log.error(dateTimeException.getMessage(), dateTimeException.getCause());
        return Response.error("日期转换异常");
    }

    @ExceptionHandler({SQLException.class})
    public
    @ResponseBody
    Response handleSQLException(SQLException sqlException) {
        log.error(sqlException.getMessage(), sqlException.getCause());
        return Response.error("数据异常");
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public
    @ResponseBody
    Response handleSQLException(HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        log.error(httpRequestMethodNotSupportedException.getMessage(), httpRequestMethodNotSupportedException.getCause());
        return Response.error("不支持的请求方式");
    }

}
