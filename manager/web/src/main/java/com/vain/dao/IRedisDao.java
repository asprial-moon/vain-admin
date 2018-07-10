package com.vain.dao;

import java.util.List;
import java.util.Set;

/**
 * @author vain
 * @Description
 * @date 2018/6/3 15:54
 */
public interface IRedisDao {

    /**
     * 添加
     *
     * @param key
     * @param value
     * @param time
     * @return
     */
    boolean cacheValue(String key, String value, long time);

    /**
     * 添加
     *
     * @param key
     * @param value
     * @return
     */
    boolean cacheValue(String key, String value);

    /**
     * 添加
     *
     * @param key
     * @param key
     * @return
     */
    boolean containsKey(String key);


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    String getValue(String key);


    /**
     * 缓存Set
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheSet(String k, Set<String> v, long time);

    /**
     * 缓存Set
     *
     * @param key
     * @param value
     * @return
     */
    boolean cacheSet(String key, Set<String> value);


    /**
     * 获取Set
     *
     * @param k
     * @return
     */
    Set<String> getSet(String k);


    /**
     * 缓存List
     *
     * @param k
     * @param v
     * @param time
     * @return
     */
    boolean cacheList(String k, List<String> v, long time);

    /**
     * 缓存List
     *
     * @param k
     * @param v
     * @return
     */
    boolean cacheList(String k, List<String> v);

    /**
     * 获取List
     *
     * @param k
     * @param start
     * @param end
     * @return
     */
    List<String> getList(String k, long start, long end);

    /**
     * 截取集合指定元素保留长度内的值
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    boolean trim(String key, long start, long end);

    /**
     * 获取页码
     *
     * @param key
     * @return
     */
    long getListSize(String key);

    /**
     * 移除list缓存
     *
     * @param k
     * @return
     */
    boolean removeOneOfList(String k);

    /**
     * 移除list缓存
     *
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     * 向集合右边添加新元素
     *
     * @param key
     * @param value
     * @return
     */
    boolean rightPush(String key, String value);

    /**
     * 向集合左边添加新元素
     *
     * @param key
     * @param value
     * @return
     */
    boolean leftPush(String key, String value);

}
