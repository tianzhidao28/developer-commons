package cn.jpush.commons.utils.pool;

import cn.jpush.commons.utils.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

public class JRedisPool {

    private static final Logger LOG = LoggerFactory.getLogger(JRedisPool.class);
    private static Map<String, JedisPool> poolMap = new HashMap<>();
    private static JedisPoolConfig poolConfig = new JedisPoolConfig();

    static {
        poolConfig.setMaxTotal(SystemConfig.getIntProperty("redis.max.total",300));
        // #最大空闲数
        poolConfig.setMaxIdle(SystemConfig.getIntProperty("redis.max.idle",100));
        poolConfig.setMinIdle(SystemConfig.getIntProperty("redis.min.idle",0));
    }

    public static void register(String key) {
        try {
            String host = SystemConfig.getProperty(key + ".redis.host");
            int port = SystemConfig.getIntProperty(key + ".redis.port");
            JedisPool pool = new JedisPool(poolConfig, host, port, 30000);
            poolMap.put(key, pool);
        } catch (Exception e) {
            LOG.error("Failed to register redis " + key, e);
        }
    }

    public static Jedis getClient(String key) {
        if(!poolMap.containsKey(key)) {
            register(key);
        }
        try{
            return poolMap.get(key).getResource();
        } catch (Exception e) {
            LOG.error("Failed to get redis client " + key, e);
        }
        return null;
    }

    public static void returnClient(String key, Jedis jedis) {
        try{
            poolMap.get(key).returnResource(jedis);
        } catch (Exception e) {
            LOG.error("Failed to return client " + key, e);
        }
    }

    public static void destroyClient(String key, Jedis jedis) {
        try {
            poolMap.get(key).returnBrokenResource(jedis);
        } catch (Exception e) {
            LOG.error("Failed to destroy jedis " + key, e);
        }
    }


}
