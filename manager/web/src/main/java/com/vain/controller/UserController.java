package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.common.ErrorCodeException;
import com.vain.constant.SystemConfigKeys;
import com.vain.dao.IRedisDao;
import com.vain.entity.User;
import com.vain.enums.StatusCode;
import com.vain.log.OperationLog;
import com.vain.log.constant.LogConstants;
import com.vain.service.IUserService;
import com.vain.shiro.exception.AuthenticationException;
import com.vain.shiro.token.AccountToken;
import com.vain.util.TokenUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author vain
 * @Description 账号信息操作controller
 * @date 2017/8/31 11:54
 */
@RequestMapping("/user")
@RestController
public class UserController extends AbstractBaseController<User> {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRedisDao redisDao;

    /**
     * 获取账号列表 （超级管理员不在此列） 分页
     *
     * @param user 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getPagedList")
    public Response<User> getPagedList(@RequestBody User user) {
        return new Response<User>().setData(userService.getPagedList(user));
    }

    /**
     * 获取账户详细信息
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/get")
    public Response<User> get(@RequestBody User entity, HttpServletRequest request) {
        entity = new User();
        //只获取自己信息
        Integer currentUserId = getCurrentUserId(request);
        if (null == currentUserId) {
            return new Response<>();
        }
        entity.setId(currentUserId);
        User user = userService.get(entity);
        user.clearSecretField();
        return new Response<User>().setData(user);
    }

    /**
     * 添加新账号
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/add")
    public Response<User> add(@RequestBody User entity) {
        if (entity == null || entity.getPassword() == null || entity.getUserName() == null || entity.getType() == 0) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<User>().setData(userService.add(entity));
    }

    /**
     * 修改账号部分信息
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modify")
    @OperationLog(operationType = LogConstants.OperationLogType.OPERATION_UPDATE, info = "修改账号信息", isOnlyId = true)
    public Response<User> modify(@RequestBody User entity, HttpServletRequest request) {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<User>().setData(userService.modify(entity));
    }

    /**
     * 修改个人信息
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modifyPersonInfo")
    public Response<User> modifyPersonInfo(@RequestBody User entity, HttpServletRequest request) {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        if (!entity.getId().equals(getCurrentUserId(request))) {
            throwNewErrorCodeException(StatusCode.FORBIDDEN);
        }
        return new Response<User>().setData(userService.modify(entity));
    }

    /**
     * 删除账号 （更新标识符）
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public Response<User> delete(@RequestBody User entity) {
        if (entity == null || (entity.getId() == null && entity.getIds() == null)) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response<User>().setData(userService.delete(entity));
    }

    /**
     * 用户登录
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/login")
    @OperationLog(operationType = LogConstants.OperationLogType.OPERATION_LOGIN, info = "登录")
    public Response<User> login(@RequestBody User entity) throws ErrorCodeException {
        if (entity == null || entity.getUserName() == null || entity.getPassword() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        Response<User> response = new Response<>();
        AccountToken token = new AccountToken(entity.getUserName(), entity.getPassword());
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(token);
        } catch (AuthenticationException authenticationException) {
            if (authenticationException.getMessage() != null) {
                response.setCode(Integer.parseInt(authenticationException.getMsgCode()));
                response.setMessage(authenticationException.getMessage());
            } else {
                response.setStatusCode(StatusCode.ACCOUNT_PASSWORD_ERROR);
            }
            return response;
        }
        User user = userService.get(entity);
        if (null == user) {
            throwNewErrorCodeException(StatusCode.ACCOUNT_NOT_EXIST);
        }
        Map<String, Object> claims = new HashMap<>(3);
        claims.put("id", user.getId());
        claims.put("name", user.getUserName());
        claims.put("type", user.getType());
        String jwtToken = TokenUtils.generateToken(claims, TokenUtils.subject);
        //缓存token
        redisDao.cacheValue(jwtToken, "", TokenUtils.expiration);
        Map<String, Object> data = new HashMap<>(4);
        user.clearSecretField();
        data.put("user", user);
        data.put(SystemConfigKeys.REQUEST_TOKEN, jwtToken);
        response.setData(data);
        return response;
    }

    /**
     * 用户注销 移除缓存
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/logout")
    public Response logout(HttpServletRequest request) {
        //移除权限缓存
        redisDao.remove(request.getHeader(SystemConfigKeys.REQUEST_TOKEN));
        return Response.success();
    }

    /**
     * 重置用户密码
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/resetPassword")
    public Response resetPwd(@RequestBody User entity) throws ErrorCodeException {
        if (entity == null || entity.getNewpasswd() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response().setData(userService.resetPwd(entity));
    }

    /**
     * 修改用户密码
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/modifyPassword")
    public Response modifyPassword(@RequestBody User entity, HttpServletRequest request) throws ErrorCodeException {
        if (entity == null || entity.getNewpasswd() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        entity.setId(getCurrentUserId(request));
        return new Response().setData(userService.resetPwd(entity));
    }


    /**
     * 锁定 / 解锁 账号
     *
     * @param entity
     * @return
     */
    @OperationLog(operationType = LogConstants.OperationLogType.OPERATION_LOGIN, info = "锁定/解锁")
    @PostMapping(value = "/lock")
    public Response lockUser(@RequestBody User entity, HttpServletRequest request) throws ErrorCodeException {
        if (entity == null || (entity.getId() == null && entity.getIds() == null)) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        return new Response().setData(userService.lock(entity));
    }

    /**
     * 分配用户权限
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/assignUserMenu")
    public Response<User> assignUserMenu(@RequestBody User entity) {
        return new Response<User>().setData(userService.assignUserMenu(entity));
    }
}
