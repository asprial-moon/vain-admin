package com.vain.dao.impl;

import com.vain.dao.IRedisDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author vain
 * @Description
 * @date 2018/6/3 15:53
 */
@Repository
@Slf4j
public class RedisDaoImpl implements IRedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisDaoImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存value操作
     *
     * @param key   key
     * @param value value
     * @param time  time
     * @return boolean
     */
    @Override
    public boolean cacheValue(String key, String value, long time) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            valueOps.set(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            log.error("cache [" + key + "] fail, value[" + value + "] , time[ " + t + "]");
        }
        return false;
    }

    /**
     * 缓存value操作
     *
     * @param key   key
     * @param value value
     * @return boolean
     */
    @Override
    public boolean cacheValue(String key, String value) {
        return cacheValue(key, value, -1);
    }

    /**
     * 判断缓存是否存在
     *
     * @param key
     * @return boolean
     */
    @Override
    public boolean containsKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取缓存
     *
     * @param key key
     * @return string
     */
    @Override
    public String getValue(String key) {
        try {
            ValueOperations<String, String> valueOps = redisTemplate.opsForValue();
            return valueOps.get(key);
        } catch (Throwable t) {
            log.error("获取缓存失败key[" + key + ", Codeor[" + t + "]");
        }
        return null;
    }

    /**
     * 缓存set
     *
     * @param key   key
     * @param value
     * @param time  time
     * @return boolean
     */
    @Override
    public boolean cacheSet(String key, Set<String> value, long time) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            setOps.add(key, value.toArray(new String[value.size()]));
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            log.error("cache [" + key + "] fail, value[" + value + "] , time[ " + t + "]");
        }
        return false;
    }

    @Override
    public boolean cacheSet(String key, Set<String> value) {
        return cacheSet(key, value, -1);
    }

    /**
     * 获取缓存set数据
     *
     * @param key key
     * @return set
     */
    @Override
    public Set<String> getSet(String key) {
        try {
            SetOperations<String, String> setOps = redisTemplate.opsForSet();
            return setOps.members(key);
        } catch (Throwable t) {
            log.error("get [" + key + "] fail");
        }
        return null;
    }


    /**
     * 缓存list
     *
     * @param key   key
     * @param value value
     * @param time  time
     * @return boolean
     */
    @Override
    public boolean cacheList(String key, List<String> value, long time) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPushAll(key, value);
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Throwable t) {
            log.error("cache [" + key + "] fail, value[" + value + "] , time[ " + t + "]");
        }
        return false;
    }

    /**
     * 缓存list
     *
     * @param key   key
     * @param value value
     * @return boolean
     */
    @Override
    public boolean cacheList(String key, List<String> value) {
        return cacheList(key, value, -1);
    }

    /**
     * 获取list缓存
     *
     * @param key   key
     * @param start start
     * @param end   end
     * @return list
     */
    @Override
    public List<String> getList(String key, long start, long end) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.range(key, start, end);
        } catch (Throwable t) {
            log.error("get [" + key + "] fail");
        }
        return null;
    }

    @Override
    public boolean trim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key, start, end);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("trim [" + key + "] fail");
        }
        return false;
    }

    /**
     * 获取总条数, 可用于分页
     *
     * @param key
     * @return long
     */
    @Override
    public long getListSize(String key) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            return listOps.size(key);
        } catch (Throwable t) {
            log.error("get [" + key + "] size  fail");
        }
        return 0;
    }

    /**
     * 移除list缓存
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean removeOneOfList(String key) {
        try {
            ListOperations<String, String> listOps = redisTemplate.opsForList();
            listOps.rightPop(key);
            return true;
        } catch (Throwable t) {
            log.error("remove cache key [" + key + "] fail");
        }
        return false;
    }

    /**
     * 移除缓存
     *
     * @param key key
     * @return boolean
     */
    @Override
    public boolean remove(String key) {
        try {
            redisTemplate.delete(key);
            return true;
        } catch (Throwable t) {
            log.error("remove cache key [" + key + "] fail");
        }
        return false;
    }

    @Override
    public boolean rightPush(String key, String value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean leftPush(String key, String value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
