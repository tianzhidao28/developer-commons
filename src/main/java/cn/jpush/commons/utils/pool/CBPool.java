package cn.jpush.commons.utils.pool;

import cn.jpush.alarm.AlarmClient;
import cn.jpush.commons.utils.config.SystemConfig;
import com.couchbase.client.CouchbaseClient;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class CBPool {

    private static Logger LOG = LoggerFactory.getLogger(CBPool.class);
    
    private static GenericObjectPoolConfig config = null;

    private static Map<String, ObjectPool<CouchbaseClient>> poolMap = new HashMap<>();

    static {
        config = new GenericObjectPoolConfig();
        config.setMaxTotal(SystemConfig.getIntProperty("couchbase.max.total",800));
        config.setMaxIdle(SystemConfig.getIntProperty("couchbase.max.idle",100));
        config.setMinIdle(SystemConfig.getIntProperty("couchbase.min.idle",50));
        config.setMinEvictableIdleTimeMillis(SystemConfig.getIntProperty("couchbase.minEvictableIdleTimeMillis",300000));
        config.setTimeBetweenEvictionRunsMillis(SystemConfig.getIntProperty("couchbase.timeBetweenEvictionRunsMillis",600000));
    }

    public static void register(String key) {
        try {
            ObjectPool<CouchbaseClient> pool = new GenericObjectPool<>(new CBFactory(key), config);
            poolMap.put(key, pool);
            LOG.info("register couchbase pool key={} successfully!",key);
        } catch (Exception e) {
            LOG.error("init cb pool error", e);
        }
    }

    public static CouchbaseClient getClient(String key) {
        synchronized (poolMap) {
            if (!poolMap.containsKey(key)) {
                register(key);
            }
        }
        try {
            return poolMap.get(key).borrowObject();
        } catch (Exception e) {
            LOG.error("Failed to get connection " + key, e);
            AlarmClient.send(84,String.format("[CB:%s] poolMap.get init error ",key));

        }
        return null;
    }
    
    public static void returnClient(String key, CouchbaseClient client) {
        try {
            poolMap.get(key).returnObject(client);
        } catch (Exception e) {
            LOG.error("Failed to return connection " + key, e);
        }
    }
    
    public static void destroyClient(String key, CouchbaseClient client) {
        try {
            poolMap.get(key).invalidateObject(client);
        } catch (Exception e) {
            LOG.error("Failed to destroy client " + key, e);
            AlarmClient.send(84,String.format("[CB:%s] poolMap.get invalidateObject error ",key));
        }
    }
}
