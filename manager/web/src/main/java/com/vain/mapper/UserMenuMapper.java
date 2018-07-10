package com.vain.mapper;

import com.vain.base.dao.BaseDao;
import com.vain.entity.UserMenu;

import java.util.List;

/**
 * @author vain
 * @Descritpion user-menu 关联对应的dao类
 * @Date 21:47 2017/10/13
 */
public interface UserMenuMapper extends BaseDao<UserMenu> {

    /**
     * 分配用户权限
     *
     * @param list
     */
    int assignUserMenu(List<UserMenu> list);

}
