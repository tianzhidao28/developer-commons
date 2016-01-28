package cn.jpush.commons.utils.pool;

import cn.jpush.commons.utils.config.SystemConfig;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class HBasePool {

    private static Logger LOG = LoggerFactory.getLogger(HBasePool.class);

    static GenericObjectPoolConfig hbaseConfig = new GenericObjectPoolConfig();
    static Map<String, ObjectPool<HTableInterface>> poolMap = new HashMap<>();

    static {
        hbaseConfig.setMaxTotal(SystemConfig.getIntProperty("hbase.max.total"));
        hbaseConfig.setMaxIdle(SystemConfig.getIntProperty("hbase.max.idle"));
        hbaseConfig.setMinIdle(SystemConfig.getIntProperty("hbase.min.idle"));
        hbaseConfig.setMinEvictableIdleTimeMillis(SystemConfig.getIntProperty("hbase.minEvictableIdleTimeMillis"));
        hbaseConfig.setTimeBetweenEvictionRunsMillis(SystemConfig.getIntProperty("hbase.timeBetweenEvictionRunsMillis"));
        LOG.info("init hbase config success!");
    }

    public static void register(String tableName) {
        try{
            ObjectPool<HTableInterface> hbasePool = new GenericObjectPool<>(new HBaseFactory(tableName), hbaseConfig);
            poolMap.put(tableName, hbasePool);
            LOG.info("Register " + tableName);
        } catch (Exception e) {
            LOG.error("Failed to register " + tableName, e);
        }
    }
    
    public static HTableInterface getHTable(String tableName) {
        if( !poolMap.containsKey(tableName) ) {
            register(tableName);
        }
        try {
            return poolMap.get(tableName).borrowObject();
        } catch (Exception e) {
            LOG.error("Failed to get HTable " + tableName, e);
            return null;
        }
    }
    
    public static void returnHTable(String tableName, HTableInterface table) {
        try {
            poolMap.get(tableName).returnObject(table);
        } catch (Exception e) {
            LOG.error("Failed to return " + tableName, e);
        }
    }
    
    public static void destroyHTable(String tableName, HTableInterface table) {
        try {
            poolMap.get(tableName).invalidateObject(table);
        } catch (Exception e) {
            LOG.error("Failed to destroy", e);
        }
    }
    
}
