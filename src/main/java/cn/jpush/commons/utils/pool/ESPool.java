package cn.jpush.commons.utils.pool;

import cn.jpush.commons.utils.config.SystemConfig;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ESPool {

    private static Logger LOG = LoggerFactory.getLogger(ESPool.class);
    
    static GenericObjectPoolConfig esConfig = new GenericObjectPoolConfig();
    public static final String USER_PROFILE = "USERPROFILE";
    private static Map<String, ObjectPool<Client>> poolMap = new HashMap<>();
    
    static {
        esConfig.setMaxTotal(SystemConfig.getIntProperty("espool.max.total"));
        esConfig.setMaxIdle(SystemConfig.getIntProperty("espool.max.idle"));
        esConfig.setMinIdle(SystemConfig.getIntProperty("espool.min.idle"));
        esConfig.setMinEvictableIdleTimeMillis(SystemConfig.getIntProperty("espool.minEvictableIdleTimeMillis"));
        esConfig.setTimeBetweenEvictionRunsMillis(SystemConfig.getIntProperty("espool.timeBetweenEvictionRunsMillis"));
    }
    
    
    public static void register(String key) {
        try {
            ObjectPool<Client> esPool = new GenericObjectPool<>(new ESFactory(), esConfig);
            poolMap.put(key, esPool);
        } catch (Exception e) {
            LOG.error("Failed to register espool " + key, e);
        }
    }

    public static Client getClient(String key) {
        if (!poolMap.containsKey(key)) {
            register(key);
        }
        try {
            return poolMap.get(key).borrowObject();
        } catch (Exception e) {
            LOG.error("get client error", e);
            return null;
        }
    }

    public static void returnClient(String key, Client client) {
        try {
            poolMap.get(key).returnObject(client);
        } catch (Exception e) {
            LOG.error("return es client error", e);
        }
    }
    
    public static void destoryClient(String key, Client client) {
        try {
            poolMap.get(key).invalidateObject(client);
        } catch (Exception e) {
            LOG.error("destory es client error", e);
        }
    }
}
