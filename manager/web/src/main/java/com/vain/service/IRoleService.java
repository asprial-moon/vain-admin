package com.vain.service;

import com.vain.base.service.BaseService;
import com.vain.entity.Menu;
import com.vain.entity.Role;
import com.vain.entity.UserRole;

import java.util.List;

/**
 * @author vain
 * @date 2017/10/9 20:00
 * @description
 */
public interface IRoleService extends BaseService<Role> {
    /**
     * 给角色分配权限菜单
     *
     * @param entity
     */
    int assignRoleMenu(Role entity);

    /**
     * 给账号分配角色
     *
     * @param entity
     * @return
     */
    int grantUserRole(UserRole entity);

    /**
     * 获取角色权限
     *
     * @param roleId
     * @return
     */
    List<Menu> getMenuByRoleId(Integer roleId);
}
