package cn.jpush.commons.utils.cache.hbase;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseFactory extends BasePooledObjectFactory<HTableInterface>{
    
    private static Logger LOG = LoggerFactory.getLogger(HBaseFactory.class);
    
    private static Configuration conf;
    private static HConnection connection;

    private final String tableName;

    public HBaseFactory(String tableName) {
        this.tableName = tableName;
    }

    static {
        initConnection();
    }
    
    public static void initConnection() {
        try {
            conf = HBaseConfiguration.create();
            long start = System.currentTimeMillis();
            connection = HConnectionManager.createConnection(conf);
            long end = System.currentTimeMillis();
            LOG.info("init hbase connection cost " + (end - start));
        } catch (Exception e) {
            LOG.error("hbase create connection error", e);
        }
    }

    @Override
    public HTableInterface create() throws Exception {
        HTableInterface hTable = null;
        try {
            long start = System.currentTimeMillis();
            if (connection == null || connection.isAborted() || connection.isClosed()) {
                initConnection();
            }
            hTable = connection.getTable(tableName);
            long end = System.currentTimeMillis();
            LOG.info("init hbase table cost " + (end - start));
        } catch (Exception e) {
            LOG.error("hbase create table error", e);
        }
        return hTable;
    }

    @Override
    public PooledObject<HTableInterface> wrap(HTableInterface table) {    
        return new DefaultPooledObject<>(table);
    }

    @Override
    public void destroyObject(PooledObject<HTableInterface> p) throws Exception {
        LOG.info("hbase table close");
        try {
            p.getObject().close();
        } catch (Exception e) {
            LOG.error("hbase close error", e);
        }
        
    }
}
