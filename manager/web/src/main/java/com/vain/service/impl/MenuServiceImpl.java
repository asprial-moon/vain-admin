package com.vain.service.impl;


import com.github.pagehelper.PageInfo;
import com.vain.base.service.AbstractBaseService;
import com.vain.common.ErrorCodeException;
import com.vain.entity.Menu;
import com.vain.entity.Role;
import com.vain.entity.User;
import com.vain.enums.*;
import com.vain.mapper.MenuMapper;
import com.vain.service.IMenuService;
import com.vain.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by vain on 2017/9/23.
 */
@Service
@Slf4j
public class MenuServiceImpl extends AbstractBaseService implements IMenuService {

    @Resource
    private MenuMapper menuMapper;

    /**
     * 仅获取用户菜单集合
     *
     * @param user 用户id
     * @return 带层级结构的菜单列表
     * @throws ErrorCodeException
     */
    @Override
    public List<Menu> getMenusByUser(User user) throws ErrorCodeException {
        Menu menu = new Menu();
        menu.setUserId(user.getId());
        List<Menu> menus = menuMapper.getMenusByUserId(menu);
        //递归返回层级结构的菜单列表
        List<Menu> returnMenus = new ArrayList<>();
        if (menus.size() > 0) {
            for (Menu data : menus) {
                if (data.getParentId() == 0) {
                    returnMenus.add(data);
                    fillMenuListChildren(menus, data);
                }
            }
        } else {
            throwErrorCodeException(StatusCode.NOT_FOUND);
        }

        return returnMenus;
    }

    @Override
    public HashSet<Menu> getMenusByUserId(Integer userId, Integer userType) {
        //去重 不包含重复元素菜单
        HashSet<Menu> userOwnedMenus = new HashSet<>();
        Menu menu = new Menu(userId);
        if (AccountType.SUPER_ADMIN.getType() == userType) {
            //超级管理员权限菜单集合
            List<Menu> menusByUserRoles = menuMapper.getList(menu);
            if (menusByUserRoles.size() > 0) {
                //去除禁用的菜单
                userOwnedMenus = menusByUserRoles.stream().filter(m -> m.getDeleted() == DeletedStatus.STATUS_NOT_DELETED.getStatus()).collect(Collectors.toCollection(HashSet::new));
            }
        } else {
            //用户所属角色菜单集合
            List<Menu> menusByUserRoles = menuMapper.getMenusByUserRoles(menu);
            if (menusByUserRoles.size() > 0) {
                userOwnedMenus.addAll(menusByUserRoles);
            }
            //用户个人菜单集合
            List<Menu> userAllMenus = menuMapper.getUserAllMenus(menu);
            if (userAllMenus.size() > 0) {
                userOwnedMenus.addAll(userAllMenus);
            }
        }
        return userOwnedMenus;
    }

    @Override
    public PageInfo<Menu> getPagedList(Menu entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public List<Menu> getList(Menu entity) throws ErrorCodeException {
        return null;
    }

    /**
     * 获取所有的菜单数据集合
     *
     * @param entity      参数实体
     * @param isHierarchy 是否返回层级结构 父子
     * @return
     * @throws ErrorCodeException
     */
    @Override
    public List<Menu> getList(Menu entity, boolean isHierarchy) throws ErrorCodeException {
        //所有菜单集合
        List<Menu> menus = menuMapper.getList(entity);
        //所有菜单集合 分层级 父子结构
        List<Menu> containsChildMenus = new ArrayList<>();
        if (menus.size() > 0) {
            if (isHierarchy) {
                for (Menu data : menus) {
                    //主菜单
                    if (data.getParentId() == 0) {
                        containsChildMenus.add(data);
                        fillMenuListChildren(menus, data);
                    }
                }
            } else {
                return menus;
            }
            log.info("初始化的总菜单个数为：" + menus.size());
        } else {
            throwErrorCodeException(StatusCode.NOT_FOUND);
        }
        return containsChildMenus;
    }

    /**
     * 获取自己菜单集合
     *
     * @param entity
     * @return
     * @throws ErrorCodeException
     */
    @Override
    public List<Menu> getMyMenus(Menu entity) throws ErrorCodeException {
        //用户的自己菜单集合  采用set不允许重复
        HashSet<Menu> userOwnedMenus = new HashSet<>();
        //返回的菜单列表 不重复 带有层级结构
        List<Menu> returnMenus = new ArrayList<>();
        /*
          用户type为1 默认角色为超级管理员 拥有其所有菜单
         */
        if (AccountType.SUPER_ADMIN.getType() == entity.getType()) {
            log.info("超级管理员登录");
            //获取管理员的所有权限
            List<Menu> adminMenus = menuMapper.getMenusByRoleKey(RoleKey.SUPER_ADMIN.getKey());
            if (!adminMenus.isEmpty()) {
                userOwnedMenus.addAll(adminMenus);
            }
            List<Menu> menus = new ArrayList<>(adminMenus);
            if (!menus.isEmpty()) {
                for (Menu data : menus) {
                    if (data.getParentId() == 0) {
                        returnMenus.add(data);
                        fillMenuListChildren(menus, data);
                    }
                }
            }
        } else {
            /*
             超级管理员赋值的其他类型用户 如管理组和普通用户 获取角色和个人权限集合
             */
            log.info("其他类型登录");
            //角色权限集合
            List<Menu> menusByUserAllRoles = menuMapper.getMenusByUserRoles(entity);
            if (!menusByUserAllRoles.isEmpty()) {
                userOwnedMenus.addAll(menusByUserAllRoles);
            }
            //用户权限集合
            List<Menu> userAllMenus = menuMapper.getUserAllMenus(entity);
            // 转换为list  转换结构
            if (!userAllMenus.isEmpty()) {
                userOwnedMenus.addAll(userAllMenus);
            }
            List<Menu> menus = new ArrayList<>(userOwnedMenus);
            //递归转换数据结构
            for (Menu data : menus) {
                if (data.getParentId() == 0) {
                    if (!isContainMenu(returnMenus, data)) {
                        returnMenus.add(data);
                    }
                    fillMenuListChildren(menus, data);
                }
            }
        }

        //菜单集合为空 就返回相应的code 和 msg
        if (returnMenus.isEmpty()) {
            throwErrorCodeException(StatusCode.NOT_FOUND);
        }
        //遍历菜单下子菜单 即二级菜单 并按照id进行排序
        returnMenus.forEach(menu -> {
            if (!CollectionUtils.isEmpty(menu.getChildren())) {
                menu.getChildren().sort(Comparator.comparing(Menu::getId));
            }
        });
        // 一级菜单排序
        returnMenus.sort(Comparator.comparing(Menu::getId));
        return returnMenus;
    }

    /**
     * 根据role的id获取对应角色的所有权限列表
     *
     * @param entity
     * @return
     */
    @Override
    public List<Menu> getMenusByRoleId(Role entity) throws ErrorCodeException {
        Menu menu = new Menu();
        menu.setRoleId(entity.getId());
        List<Menu> returnMenus = new ArrayList<>();
        List<Menu> menus = menuMapper.getMenusByRoleId(menu);
        if (!menus.isEmpty()) {
            for (Menu data : menus) {
                if (data.getParentId() == 0) {
                    returnMenus.add(data);
                    fillMenuListChildren(menus, data);
                }
            }
        } else {
            throwErrorCodeException(StatusCode.NOT_FOUND);
        }
        return returnMenus;
    }

    /**
     * 获取menukey 的子集菜单
     *
     * @param dataList
     * @param menuKey
     * @return
     */
    @Override
    public List<Menu> getChildMenu(List<Menu> dataList, String menuKey) {
        List<Menu> returnMenus = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dataList)) {
            for (Menu data : dataList) {
                List<Menu> children = data.getChildren();
                if (!CollectionUtils.isEmpty(children)) {
                    for (Menu child : children) {
                        if (StringUtils.isNotEmpty(child.getMenuKey()) && child.getMenuKey().equals(menuKey)) {
                            if (!CollectionUtils.isEmpty(child.getChildren())) {
                                returnMenus.addAll(child.getChildren());
                            }
                        }
                    }
                }

            }
        }
        return returnMenus;
    }

    @Override
    public Menu get(Menu entity) throws ErrorCodeException {
        return null;
    }

    @Override
    public int add(Menu entity) throws ErrorCodeException {
        return 0;
    }

    @Override
    public int modify(Menu entity) throws ErrorCodeException {
        return menuMapper.update(entity);
    }

    @Override
    public int delete(Menu entity) throws ErrorCodeException {
        return 0;
    }

    /**
     * 获取所有的菜单数据集合,采用递归操作,将子级菜单的分级的 fillMenuChildren:通过上级的菜单id，匹配其子级的菜单的parentId进行查询
     *
     * @param menu 上级菜单实体参数（菜单id）
     */
    private void fillMenuListChildren(List<Menu> menus, Menu menu) {
        List<Menu> childrenList = new ArrayList<>();
        /*
         * 遍历所有菜单集合 得到条件为父菜单id等于菜单parentId的所有子菜单 放入childrenList中，再将list集合set进父菜单的children里
         */
        for (Menu childMenu : menus) {
            if (childMenu.getParentId().equals(menu.getId())) {
                menu.setHasChildren(true);
                if (!isContainMenu(childrenList, childMenu)) {
                    childrenList.add(childMenu);
                }
                menu.setChildren(childrenList);
                if (MenuType.OPERATE.getType() != childMenu.getType()) {
                    fillMenuListChildren(menus, childMenu);
                }

            }

        }
    }


    /**
     * 是否已包含菜单
     *
     * @param menus
     * @param menu
     * @return
     */
    private boolean isContainMenu(List<Menu> menus, Menu menu) {
        if (CollectionUtils.isEmpty(menus)) {
            return false;
        }
        for (Menu data : menus) {
            if (data.getId().equals(menu.getId())) {
                return true;
            }
        }
        return false;
    }
}
