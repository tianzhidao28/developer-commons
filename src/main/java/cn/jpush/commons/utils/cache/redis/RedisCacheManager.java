package cn.jpush.commons.utils.cache.redis;

import cn.jpush.commons.utils.io.SerializeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class RedisCacheManager {
    private final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);
    private static Map<String, RedisCacheManager> cache = new HashMap<String, RedisCacheManager>();
    private String poolName;

    /**
     * 构造函数
     *
     * @param poolName 队列名
     */
    private RedisCacheManager(String poolName) {
        super();
        this.poolName = poolName;
    }

    /**
     * 获取Redis缓存管理工具类的实例
     *
     * @param poolName 队列名
     */
    public static RedisCacheManager getInstanse(String poolName) {
        RedisCacheManager redisCacheManager = cache.get(poolName);
        if (cache.get(poolName) == null) {
            redisCacheManager = new RedisCacheManager(poolName);
            cache.put(poolName, redisCacheManager);
        }
        return redisCacheManager;
    }

    /**
     * 获取jedis连接
     */
    public synchronized Jedis getJedis() {
        try {
            Jedis resource = JedisFactory.getJedisInstance(poolName);
            if (resource != null) {
                return resource;
            } else {
                log.error(String.format("[Redis] get {} redis is empty.", poolName));
                return null;
            }
        } catch (Exception e) {
            log.error(String.format("[Redis] get {} redis exception", poolName), e);
            return null;
        }
    }

    /**
     * 获取无序集合
     *
     * @param key 键
     */
    public Set<String> smembers(final String key) {
        return new RedisExecutor<Set<String>>() {
            @Override
            Set<String> execute() {
                return jedis.smembers(key);
            }
        }.getResult();
    }

    /**
     * 判断链接jedis对象是否为空
     */
    public boolean isNullRedis() {
        return getJedis() == null;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis 待释放的jedis对象
     */
    public void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            JedisFactory.release(poolName, jedis);
        }
    }

    /**
     * @param key key pattern
     * @return
     */
    public Set<String> keys(final String key) {
        return new RedisExecutor<Set<String>>() {
            @Override
            Set<String> execute() {
                return jedis.keys(key);
            }
        }.getResult();
    }

    /**
     * 设置超时时间
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(final String key, final int seconds ) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.expire(key,seconds);
            }
        }.getResult();
    }




    /**
     * 入队
     *
     * @param key   键(队列名)
     * @param value 值
     */
    public void lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            jedis.lpush(key, value);
            log.info(String.format("push data %s success, key=%s", poolName,
                    key));
        } catch (Exception e) {
            log.error(String.format("push data %s error, key=%s", poolName,
                    key), e);
        } finally {
            releaseRedis(jedis);
        }
    }

    /**
     * 出队
     *
     * @param key 键(队列名)
     */
    public String lpop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.lpop(key);
            }
        }.getResult();
    }

    /**
     * 取出队列第一个,没删除数据,0代表第一个
     *
     * @param key 键(队列名)
     */
    public String lindex(final String key,final int index) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.lindex(key, index);
            }
        }.getResult();
    }




    /**
     * 有序优先级队列入队
     *
     * @param key   键(队列名)
     * @param value 值
     * @param score 优先级
     */
    public void sortedPriorityPush(final String key, final String value, final double score) {
        new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long result = jedis.zadd(key, score, value);
                log.info("[Redis({})] push data key = {},value = {} success ;return {}", poolName,
                        key, value, result);
                return result;
            }
        }.getResult();

    }

    /**
     * 有序优先级队列入队
     *
     * @param key 键(队列名)
     */
    public String sortedPriorityPop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                Set<String> set = jedis.zrange(key, 0, 0);
                if (set.size() > 0) {
                    String value = set.iterator().next();
                    jedis.zrem(key, value);
                    return value;
                }
                return null;
            }
        }.getResult();

    }

    /**
     * sorted set 增加元素
     * @param key
     * @param score
     * @param member
     * @return
     */
    public Long zadd(final String key, final double score, final String member) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zadd(key, score, member);
            }
        }.getResult();
    }

    /**
     * @param key
     * @param min
     * @param max
     * @return
     */
    public Long zcount(final String key, final double min, final double max) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zcount(key, min, max);
            }
        }.getResult();
    }

    /**
     * @param key
     * @param field
     * @return
     */
    public String hget(final String key, final String field) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.hget(key, field);
            }
        }.getResult();
    }

    public Map<String, String> hgetAll(final String key) {
        return new RedisExecutor<Map<String, String>>() {
            @Override
            Map<String, String> execute() {
                return jedis.hgetAll(key);
            }
        }.getResult();
    }

    /**
     * @param key
     * @param field
     * @return
     */
    public Long hset(final String key, final String field, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hset(key, field, value);
            }
        }.getResult();
    }

    /**
     * hash
     *
     * @param key
     * @param fieldMap
     * @return
     */
    public String hmset(final String key, final HashMap<String, String> fieldMap) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.hmset(key, fieldMap);
            }
        }.getResult();
    }

    /**
     * Set
     *
     * @param key
     * @param value
     * @return
     */
    public String set(final String key, final String value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.set(key, value);
            }
        }.getResult();
    }

    public String setex(final String key,final int seconds, final String value ) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.setex(key, seconds, value);
            }
        }.getResult();
    }

    /**
     * jdk 序列化set
     * @param key key.getBytes()
     * @param seconds
     * @param value 序列化
     * @return
     */
    public String setObjectEx(final String key,final int seconds, final Object value ) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.setex(key.getBytes(), seconds ,SerializeUtils.serialize(value));
            }
        }.getResult();
    }


    /**
     * 获取序列化存储的值
     * @param key
     * @return
     */
    public Object getObject(final String key) {
        return new RedisExecutor<Object>() {
            @Override
            Object execute() {
                return SerializeUtils.deserialize(jedis.get(key.getBytes()));
            }
        }.getResult();
    }


    public String get(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.get(key);
            }
        }.getResult();
    }


    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public Long del(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.del(key);
            }
        }.getResult();
    }


    public Long decrBy(final String key, final Long decValue) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long retValue = jedis.decrBy(key, decValue);
                log.info("[Redis] decr key={},decrValue={},newValue={}", key, decValue, retValue);
                return retValue;
            }
        }.getResult();
    }

    public Long incrBy(final String key, final Long decValue) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long retValue = jedis.incrBy(key, decValue);
                log.info("[Redis] decr key={},decrValue={},newValue={}", key, decValue, retValue);
                return retValue;
            }
        }.getResult();
    }

    public Long incr(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long retValue = jedis.incr(key);
                log.info("[Redis] decr key={},newValue={}", key, retValue);
                return retValue;
            }
        }.getResult();
    }

    public String clientList() {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                String retValue = jedis.clientList();
                log.info("[Redis({})] clientList = {}", poolName, retValue);
                return retValue;
            }
        }.getResult();
    }


    /**
     * long == null 代表列表不存在
     *
     * @param key
     * @return
     */
    public Long llen(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                long retValue = jedis.llen(key);
                log.info("[Redis({})] clientList = {}", poolName, retValue);
                return retValue;
            }
        }.getResult();
    }


    public Long pub(final byte[] topic, final byte[] data) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long retuVal = jedis.publish(topic, data);
                log.info("[Redis({})] pub = {}", poolName, retuVal);
                return retuVal;
            }
        }.getResult();
    }

    abstract class RedisExecutor<T> {
        Jedis jedis;

        public RedisExecutor() {
            jedis = getJedis();
        }

        abstract T execute();

        /**
         * 调用{@link #execute()}并返回执行结果
         * 它保证在执行{@link #execute()}之后释放数据源returnResource(jedis)
         *
         * @return 执行结果
         */
        public T getResult() {
            T result = null;
            try {
                result = execute();
            } catch (Throwable e) {
                JedisFactory.realeaseBroken(poolName, jedis);
                log.error("[Redis] [poolName = {}] 执行命令时异常 ", poolName, e);
            } finally {
                JedisFactory.release(poolName, jedis);
            }
            return result;
        }
    }


}
