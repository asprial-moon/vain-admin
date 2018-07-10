package com.vain.base.dao;


import com.vain.base.entity.Entity;

import java.util.List;

/**
 * @author vain
 * @description: dao的公共接口
 * @date 2017/8/31 11:40
 */
public interface BaseDao<T extends Entity> {

    /**
     * 获取所有数据
     *
     * @param entity 参数实体
     * @return
     */
    List<T> getList(T entity);

    /**
     * 获取单条数据
     *
     * @param entity 参数实体
     * @return
     */
    T get(T entity);

    /**
     * 插入数据
     *
     * @param entity 参数实体
     * @return
     */
    int insert(T entity);

    /**
     * 更新数据
     *
     * @param entity 参数实体
     * @return
     */
    int update(T entity);

    /**
     * 删除数据
     *
     * @param entity 参数实体
     * @return
     */
    int delete(T entity);
}
