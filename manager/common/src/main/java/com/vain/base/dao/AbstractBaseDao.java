package com.vain.base.dao;


import com.vain.base.entity.Entity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author vain
 * @description: dao的抽象类
 * @date 2017/8/31 11:39
 */
@Slf4j
public abstract class AbstractBaseDao<T extends Entity> implements BaseDao<T> {

    /**
     * 数据库操作对象
     */
    @Autowired
    protected SqlSession sqlSession;

}
