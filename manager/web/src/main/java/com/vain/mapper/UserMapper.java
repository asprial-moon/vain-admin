package com.vain.mapper;

import com.vain.base.dao.BaseDao;
import com.vain.entity.User;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * @author vain
 * @description: 用户信息操作dao
 * @date 2017/8/31 11:57
 */
public interface UserMapper extends BaseDao<User> {

    /**
     * 重置密码
     *
     * @param entity
     * @return
     */
    int resetPwd(User entity);

    /**
     * 锁定 / 解锁 账号
     *
     * @param entity
     * @return
     */
    int lock(User entity);

    /**
     * 根据id 获取数据
     *
     * @param id
     * @return
     */
    User getById(@Param("id") Integer id);
}
