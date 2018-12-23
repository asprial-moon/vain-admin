package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.common.ErrorCodeException;
import com.vain.component.SysConfigComponent;
import com.vain.constant.SystemConfigKeys;
import com.vain.dao.IRedisDao;
import com.vain.entity.SystemConfig;
import com.vain.entity.User;
import com.vain.enums.StatusCode;
import com.vain.log.OperationLog;
import com.vain.log.constant.LogConstants;
import com.vain.service.IUserService;
import com.vain.shiro.exception.AuthenticationException;
import com.vain.shiro.token.AccountToken;
import com.vain.util.MD5Utils;
import com.vain.util.StringUtils;
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

    @Autowired
    private SysConfigComponent sysConfigComponent;

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
    public Response<User> get(@RequestBody User entity) {
        entity = new User();
        //只获取自己信息
        Integer currentUserId = getCurrentUserId();
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
        claims.put("type", user.getType());
        String jwtToken = TokenUtils.generateToken(claims, TokenUtils.subject);
        //缓存token
        redisDao.cacheValue(jwtToken, "", TokenUtils.expiration);
        Map<String, Object> data = new HashMap<>(4);
        user.clearSecretField();
        data.put("user", user);
        data.put(SystemConfigKeys.REQUEST_TOKEN, jwtToken);
        //项目环境
        SystemConfig environment = sysConfigComponent.getSystemConfig(SystemConfigKeys.SYS_ENVIRONMENT);
        data.put("environment", environment);
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
     * 修改用户信息
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/modifyPersonInfo")
    public Response modifyPersonInfo(@RequestBody User entity) throws ErrorCodeException {
        if (entity == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        Integer currentUserId = this.getCurrentUserId();
        User dbUser = userService.findById(currentUserId);
        if (null == dbUser) {
            throwNewErrorCodeException(StatusCode.ACCOUNT_NOT_EXIST);
        }
        if (!dbUser.getId().equals(currentUserId)) {
            throwNewErrorCodeException(StatusCode.FORBIDDEN);
        }
        entity.setId(currentUserId);
        entity.clearSecretField();
        entity.setUserName("");
        return new Response().setData(userService.modify(entity));
    }

    /**
     * 修改个人登录密码
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/modifyPersonPassword")
    public Response modifyPersonPassword(@RequestBody User entity) throws ErrorCodeException {
        if (entity == null || StringUtils.isEmpty(entity.getNewpasswd())) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        if (StringUtils.isEmpty(entity.getPassword())) {
            throwNewErrorCodeException(StatusCode.ACCOUNT_PASSWORD_ERROR);
        }
        Integer currentUserId = getCurrentUserId();
        if (null != currentUserId) {
            entity.setId(currentUserId);
            User dbUser = userService.findById(currentUserId);
            if (null == dbUser) {
                throwNewErrorCodeException(StatusCode.ACCOUNT_NOT_EXIST);
            }
            String md5Encrypt = MD5Utils.getMD5Str(entity.getPassword() + dbUser.getSalt());
            if (!dbUser.getPassword().equals(md5Encrypt)) {
                throwNewErrorCodeException(StatusCode.ACCOUNT_PASSWORD_ERROR);
            }
        }
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
