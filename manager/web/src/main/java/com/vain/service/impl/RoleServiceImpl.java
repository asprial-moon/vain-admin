package com.vain.service.impl;

import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.Menu;
import com.vain.entity.Role;
import com.vain.entity.RoleMenu;
import com.vain.entity.UserRole;
import com.vain.enums.DeletedStatus;
import com.vain.mapper.RoleMapper;
import com.vain.mapper.RoleMenuMapper;
import com.vain.mapper.UserRoleMapper;
import com.vain.service.IRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author vain
 * @date 2017/10/9 20:01
 * @description 角色管理具体service类
 */
@Service
public class RoleServiceImpl extends AbstractBaseService implements IRoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource
    private UserRoleMapper userRoleMapper;


    @Override
    public PageInfo<Role> getPagedList(Role entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public List<Role> getList(Role entity) throws ErrorCodeException {
        return roleMapper.getList(entity);
    }

    @Override
    public Role get(Role entity) throws ErrorCodeException {
        return roleMapper.get(entity);
    }

    @Override
    public int add(Role entity) throws ErrorCodeException {
        return roleMapper.insert(entity);
    }

    @Override
    public int modify(Role entity) throws ErrorCodeException {
        return roleMapper.update(entity);
    }

    @Override
    public int delete(Role entity) throws ErrorCodeException {
        entity.setDeleted(DeletedStatus.STATUS_DELETED.getStatus());
        return roleMapper.delete(entity);
    }

    /**
     * 分配角色菜单权限时候 先删除原有权限在添加新的权限
     *
     * @param role
     */
    @Override
    public int assignRoleMenu(Role role) {
        //删除
        roleMenuMapper.delete(new RoleMenu(role.getId()));
        List<Menu> menus = role.getMenus();
        List<RoleMenu> roleMenus = new ArrayList<>();
        for (Menu menu : menus) {
            assignRoleMenu(menu, role, roleMenus);
        }
        if (!roleMenus.isEmpty()) {
            //批量插入
            return roleMenuMapper.assignRoleMenu(roleMenus);
        }
        return 0;
    }

    /**
     * 之前有角色就更新 没有就添加
     *
     * @param userRole
     * @return
     */
    @Override
    public int grantUserRole(UserRole userRole) {
        UserRole dbUserRole = new UserRole();
        dbUserRole.setUserId(userRole.getUserId());
        //获取userRole在数据库的ID
        dbUserRole = userRoleMapper.get(dbUserRole);
        if (dbUserRole != null && dbUserRole.getId() != null) {
            userRole.setId(dbUserRole.getId());
            return userRoleMapper.update(userRole);
        } else {
            return userRoleMapper.insert(userRole);
        }
    }

    @Override
    public List<Menu> getMenuByRoleId(Integer roleId) {
        return roleMenuMapper.getMenuByRoleId(roleId);
    }

    /**
     * 递归遍历menu  不受菜单层级限制
     */
    private void assignRoleMenu(Menu menu, Role role, List<RoleMenu> roleMenus) {
        if (menu.getHasPermission()) {
            //添加
            roleMenus.add(new RoleMenu(role.getId(), menu.getId()));
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                for (Menu data : menu.getChildren()) {
                    assignRoleMenu(data, role, roleMenus);
                }
            }
        }

    }
}
