package com.vain.controller;

import com.vain.base.controller.AbstractBaseController;
import com.vain.base.entity.Response;
import com.vain.entity.Menu;
import com.vain.entity.Role;
import com.vain.entity.UserRole;
import com.vain.enums.StatusCode;
import com.vain.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author vain
 * @date 2017/10/9 20:04
 * @description 角色接口
 */
@RequestMapping("role")
@RestController
public class RoleController extends AbstractBaseController<Role> {

    @Autowired
    private IRoleService roleService;


    /**
     * 获取角色参数列表
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/getList")
    public Response<Role> getList(@RequestBody Role entity) {
        return new Response<Role>().setDataList(roleService.getList(entity));
    }

    /**
     * 通过id 获取对应的角色信息
     *
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getById/{id}")
    public Response<Role> getById(@PathVariable Integer id) {
        Role role = new Role();
        role.setId(id);
        role = roleService.get(role);
        role.setMenus(roleService.getMenuByRoleId(id));
        return new Response<Role>().setData(role);
    }

    /**
     * 根据角色id分配菜单权限
     *
     * @param entity
     * @return
     */
    @PostMapping(value = "/assignRoleMenu")
    public Response<Role> assignRoleMenu(@RequestBody Role entity) {
        if (entity == null || entity.getId() == null) {
            return new Response<>(StatusCode.PARAMETER_ERROR);
        }
        return new Response<Role>().setData(roleService.assignRoleMenu(entity));
    }

    /**
     * 添加角色
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/add")
    public Response<Role> add(@RequestBody Role entity) {
        return new Response<Role>().setData(roleService.add(entity));
    }

    /**
     * 修改角色
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/modify")
    public Response<Role> modify(@RequestBody Role entity) {
        if (entity == null || entity.getId() == null) {
            return new Response<>(StatusCode.PARAMETER_ERROR);
        }
        return new Response<Role>().setData(roleService.modify(entity));
    }

    /**
     * 删除某一角色
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/delete")
    public Response<Role> delete(@RequestBody Role entity) {
        if (entity == null || entity.getId() == null) {
            return new Response<>(StatusCode.PARAMETER_ERROR);
        }
        return new Response<Role>().setData(roleService.delete(entity));
    }

    /**
     * 给账号赋予角色
     *
     * @param entity 参数实体
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/grantUserRole")
    public Response<UserRole> grantUserRole(@RequestBody UserRole entity) {
        if (entity == null || entity.getId() == null || entity.getUserId() == null) {
            return new Response<>(StatusCode.PARAMETER_ERROR);
        }
        return new Response<UserRole>().setData(roleService.grantUserRole(entity));
    }
}
