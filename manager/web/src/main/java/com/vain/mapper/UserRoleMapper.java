package com.vain.mapper;

import com.vain.base.dao.BaseDao;
import com.vain.entity.UserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author vain
 * @date： 2017/10/21 18:05
 * @description： t_user_role 操作dao类
 */
public interface UserRoleMapper extends BaseDao<UserRole> {


    /**
     * 删除用户全部角色
     *
     * @param userId
     */
    int deleteUserAllRole(@Param(value = "userId") Integer userId);

    /**
     * 插入用户角色
     *
     * @param userRoleList
     */
    int insertUserRole(List<UserRole> userRoleList);
}
