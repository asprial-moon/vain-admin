package com.vain.base.controller;


import com.vain.base.entity.Entity;
import com.vain.common.ErrorCodeException;
import com.vain.enums.StatusCode;
import com.vain.util.HttpContext;
import com.vain.util.StringUtils;
import com.vain.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author vain
 * @description: controller 的抽象类
 * @date 2017/8/31 11:38
 */
@Slf4j
public abstract class AbstractBaseController<T extends Entity> implements BaseController<T> {

    protected void throwNewErrorCodeException(StatusCode statusCode) {
        throw new ErrorCodeException(statusCode.getCode(), statusCode.getMessage());
    }

    protected Integer getCurrentUserId() {
        HttpServletRequest request = HttpContext.getRequest();
        if (null != request) {
            String token = request.getHeader("Token");
            if (StringUtils.isEmpty(token)) {
                return null;
            }
            Claims claim = TokenUtils.getClaimFromToken(token);
            if (null == claim) {
                return null;
            }
            return (Integer) claim.get("id");
        }
        return 0;
    }

}
