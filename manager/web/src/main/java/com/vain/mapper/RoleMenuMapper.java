package com.vain.mapper;

import com.vain.base.dao.BaseDao;
import com.vain.entity.Menu;
import com.vain.entity.RoleMenu;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author vain
 * @date： 2017/10/11 15:14
 * @description： role-menu关联dao类
 */
public interface RoleMenuMapper extends BaseDao<RoleMenu> {


    /**
     * 分配角色权限
     *
     * @param list
     */
    int assignRoleMenu(List<RoleMenu> list);

    /**
     * 获取角色权限
     *
     * @return
     */
    List<Menu> getMenuByRoleId(@Param("roleId") Integer roleId);
}
