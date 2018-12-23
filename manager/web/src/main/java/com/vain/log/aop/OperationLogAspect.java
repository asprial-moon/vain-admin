package com.vain.log.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vain.constant.SystemConfigKeys;
import com.vain.dao.IRedisDao;
import com.vain.entity.User;
import com.vain.log.OperationLog;
import com.vain.log.constant.LogConstants;
import com.vain.pool.ThreadPool;
import com.vain.service.IUserService;
import com.vain.util.HttpContext;
import com.vain.util.StringUtils;
import com.vain.util.TokenUtils;
import com.vain.utils.FieldsUtils;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author vain
 * @date： 2017/11/3 11:23
 * @description： 如出现aop拦截方法执行2次 检查bean的生成是否重复
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private IRedisDao redisDao;

    @Autowired
    private ThreadPool pool;

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(OperationLogAspect.class);

    /**
     * 切点
     */
    @Pointcut("@annotation(com.vain.log.OperationLog)")
    public void logPoint() {
    }

    /**
     * 前置通知
     */
    @Before("logPoint()")
    public void before(JoinPoint joinPoint) {
    }

    @AfterReturning(pointcut = "logPoint()")
    public void doAfter(JoinPoint joinPoint) {
        //在当前线程先获取请求中信息
        com.vain.entity.OperationLog log = new com.vain.entity.OperationLog();
        setParameterFromRequest(log);
        pool.execute(() -> logServiceHandler(joinPoint, null, log));
    }

    /**
     * 切面操作
     *
     * @param joinPoint
     * @param e
     */
    private void logServiceHandler(JoinPoint joinPoint, Exception e, com.vain.entity.OperationLog log) {
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("注解只能用于方法上");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method != null) {
            OperationLog operationLog = method.getAnnotation(OperationLog.class);
            if (operationLog != null) {
                try {
                    // 拦截的方法参数
                    String classType = joinPoint.getTarget().getClass().getName();
                    Class<?> clazz = Class.forName(classType);
                    String clazzName = clazz.getName();
                    String methodName = joinPoint.getSignature().getName();
                    log.setClassName(clazzName);
                    log.setMethodName(methodName);
                    logger.info(">>>>>>>>>操作类：{},操作方法{}", clazzName, methodName);
                    Object[] args = joinPoint.getArgs();
                    //可以通过foreach来遍历 存储请求的所有参数 这里请求的实体参数设置为第一个 所以只将第一个转换为String存
                    Map<String, String> map = FieldsUtils.getFieldsByReflect(args[0]);
                    if (operationLog.isOnlyId()) {
                        log.setOperationData("{id:" + map.get("id") + "}");
                        logger.info(">>>>>>>>>拦截到的参数ID为:{}", map.get("id"));
                    } else {
                        Set<Map.Entry<String, String>> entries = map.entrySet();
                        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
                        while (iterator.hasNext()) {
                            Map.Entry<String, String> next = iterator.next();
                            //不保存字段
                            if (null == next.getValue() || "".equals(next.getValue()) || "0".equals(next.getValue()) || "password".equals(next.getKey()) || "createTime".equals(next.getKey())) {
                                iterator.remove();
                            } else if ("userName".equals(next.getKey())) {
                                log.setUserName(next.getValue());
                            }
                        }
                        if (LogConstants.OperationLogType.OPERATION_LOGIN == operationLog.operationType() && StringUtils.isNotEmpty(log.getUserName()) && null == log.getId()) {
                            //登录的时候没有id
                            User user = new User();
                            user.setUserName(log.getUserName());
                            user = userService.get(user);
                            if (null != user) {
                                log.setUserId(user.getId());
                            }
                        }
                        log.setOperationData(JSONObject.toJSONString(map));
                        logger.info(">>>>>>>>>拦截到的参数为:{}", map);
                    }
                    log.setInfo(operationLog.info());
                    log.setOperationType(operationLog.operationType());
                    redisDao.rightPush(SystemConfigKeys.OPERATION_LOG_KEY, JSON.toJSONString(log));
                } catch (Exception e1) {
                    e1.printStackTrace();
                    logger.info(">>>>>>>>>异常:{}", e1.getMessage());
                }
            }
        }
    }


    /**
     * 异常拦截
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "logPoint()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        //请求方法的参数并序列化为JSON格式字符串
        com.vain.entity.OperationLog log = new com.vain.entity.OperationLog();
        setParameterFromRequest(log);
        pool.execute(() -> {
            try {
                String classType = joinPoint.getTarget().getClass().getName();
                Class<?> clazz = Class.forName(classType);
                String clazzName = clazz.getName();
                String methodName = joinPoint.getSignature().getName();
                log.setClassName(clazzName);
                log.setMethodName(methodName);
                log.setExceptionMessage(e.getMessage());
                log.setInfo(e.getClass().getName());
                StringBuilder params = new StringBuilder();
                if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                    for (Object parameter : joinPoint.getArgs()) {
                        params.append(JSON.toJSONString(parameter)).append(";");
                        if (parameter instanceof HttpServletRequest) {
                            setParameterFromRequest(log);
                        }
                    }
                }
                log.setOperationData(params.toString());
                redisDao.rightPush(SystemConfigKeys.OPERATION_LOG_KEY, JSON.toJSONString(log));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        });
        logger.info(">>>>>>>>>异常捕捉:{}", e.getMessage());
    }


    /**
     * 从请求头中获取必要参数
     *
     * @param log
     */
    private void setParameterFromRequest(com.vain.entity.OperationLog log) {
        log.setOperationIP(HttpContext.getRemoteAddress());
        HttpServletRequest request = HttpContext.getRequest();
        if (null != request) {
            String token = request.getHeader(SystemConfigKeys.REQUEST_TOKEN);
            if (StringUtils.isNotEmpty(token)) {
                Claims claim = TokenUtils.getClaimFromToken(token);
                if (null != claim) {
                    log.setUserId((Integer) claim.get("id"));
                }
            }
        }

    }

}
