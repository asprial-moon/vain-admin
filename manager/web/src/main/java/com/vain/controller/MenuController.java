package com.vain.controller;

import com.alibaba.fastjson.JSON;
import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.common.ErrorCodeException;
import com.vain.dao.IRedisDao;
import com.vain.entity.Menu;
import com.vain.entity.Role;
import com.vain.entity.User;
import com.vain.enums.StatusCode;
import com.vain.service.IMenuService;
import com.vain.util.TokenUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author vain
 * @date 2017/9/23 23:12
 * @description 获取菜单的controller
 */
@RestController
@RequestMapping("menu")
@Slf4j
public class MenuController extends AbstractBaseController<Menu> {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRedisDao redisDao;

    /**
     * 获取用户自己的菜单
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/getMyMenus")
    public Response getMyMenu(@RequestBody Menu entity, HttpServletRequest request) throws ErrorCodeException {
        Response response = new Response();
        String token = request.getHeader("Token");
        //访问没有带身份(Token)信息 直接不允许
        if (StringUtils.isEmpty(token)) {
            log.error(request.getRequestURI() + " token is null");
            response.setStatusCode(StatusCode.ACCOUNT_LOGIN);
            return response;
        }
        if (TokenUtils.isTokenExpired(token)) {
            log.error(request.getRequestURI() + " token is expire");
            response.setStatusCode(StatusCode.TOKEN_INVALID);
            return response;
        }

        Map<String, Object> data = new HashMap<>(48);
        List<String> menus = new ArrayList<>();
        if (redisDao.containsKey(token)) {
            //token服务器的缓存还在
            String cacheMenu = redisDao.getValue(token);
            if (StringUtils.isEmpty(cacheMenu)) {
                Claims claim = TokenUtils.getClaimFromToken(token);
                if (null == claim) {
                    response.setStatusCode(StatusCode.TOKEN_LEGAL);
                    return response;
                }
                Integer userId = (Integer) claim.get("id");
                Integer type = (Integer) claim.get("type");
                HashSet<Menu> myMenus = menuService.getMenusByUserId(userId, type);
                for (Menu myMenu : myMenus) {
                    menus.add(myMenu.getMenuKey());
                }
                redisDao.cacheValue(token, JSON.toJSONString(menus), TokenUtils.expiration);
                log.info("reload user {} token", userId);
            } else {
                menus = JSON.parseArray(cacheMenu, String.class);
                log.info("cache user {} token");
            }
            data.put("menus", menus);
            response.setData(data);
            response.setStatusCode(StatusCode.SUCCESS);
        } else {
            //token服务器的缓存丢失（重新登录）
            response.setStatusCode(StatusCode.TOKEN_INVALID);
        }
        return response;
    }

    /**
     * 根据role角色id获取角色权限
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/getMenusByRoleId")
    public Response<Menu> getMenusByRoleId(@RequestBody Role entity) throws ErrorCodeException {
        return new Response<Menu>().setDataList(menuService.getMenusByRoleId(entity));
    }

    /**
     * 获取用户的权限菜单
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/getMenusByUserId")
    public Response<Menu> getMenusByUserId(@RequestBody User entity) throws ErrorCodeException {
        if (entity == null || entity.getId() == null) {
            throwNewErrorCodeException(StatusCode.PARAMETER_ERROR);
        }
        HashSet<Menu> menusByUserId = menuService.getMenusByUserId(entity.getId(), entity.getType());
        return new Response<Menu>().setDataList(new ArrayList<>(menusByUserId));
    }

    /**
     * 获取所有的菜单列表
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @PostMapping(value = "/getMenuList")
    public Response<Menu> getMenuList(@RequestBody Menu entity) throws ErrorCodeException {
        Response<Menu> response = new Response<>();
        response.setDataList(menuService.getList(entity, true));
        return response;
    }


    /**
     * 修改菜单
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modify")
    public Response modify(@RequestBody Menu entity) {
        menuService.modify(entity);
        return Response.success();
    }

}
