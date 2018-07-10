package com.vain.mapper;

import com.vain.entity.Menu;

import java.util.List;

/**
 * Created by vain on 2017/9/23.
 */
public interface MenuMapper {

    int insert(Menu menu);

    List<Menu> getList(Menu menu);


    int update(Menu menu);

    int delete(Menu menu);

    /**
     * 获取用户下t_user_menu所有的菜单
     *
     * @param menu userId
     * @return
     */
    List<Menu> getUserAllMenus(Menu menu);

    /**
     * 获取角色下所有菜单
     *
     * @param roleKey roleKey
     * @return
     */
    List<Menu> getMenusByRoleKey(String roleKey);

    /**
     * 获取用户下所有菜单 带hasPermission判断
     *
     * @param menu userId
     * @return
     */
    List<Menu> getMenusByUserId(Menu menu);

    /**
     * 获取角色下所有菜单 带hasPermission判断
     *
     * @param menu roleId
     * @return
     */
    List<Menu> getMenusByRoleId(Menu menu);

    /**
     * 查询用户下所有角色菜单集合
     *
     * @param menu userId
     * @return
     */
    List<Menu> getMenusByUserRoles(Menu menu);

}
