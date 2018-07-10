package com.vain.mapper;

import com.vain.base.dao.BaseDao;
import com.vain.entity.User;

/**
 * @author vain
 * @description: 用户信息操作dao
 * @date 2017/8/31 11:57
 */
public interface UserMapper extends BaseDao<User> {


    /**
     * 获取分页数据
     *
     * @param entity   参数实体
     * @param curPage  当前页码
     * @param pageSize 每页大小
     * @return
     */
/*
    public PageInfo<User> getPagedList(User entity, int curPage, int pageSize) {
        PageHelper.startPage(curPage, pageSize);
        List<User> users = userService.getList(entity);
        return new PageInfo<>(users);
    }
*/

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
}
