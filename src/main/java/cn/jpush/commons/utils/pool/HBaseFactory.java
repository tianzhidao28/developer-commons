package cn.jpush.commons.utils.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HBaseFactory extends BasePooledObjectFactory<Table>{
    
    private static Logger LOG = LoggerFactory.getLogger(HBaseFactory.class);
    
    private static Configuration conf = HBaseConfiguration.create();

    private final String tableQualifier;

    public HBaseFactory(String tableQualifier) {
        this.tableQualifier = tableQualifier;
    }

    @Override
    public Table create() throws Exception {
        Table table = null;
        try {
            long start = System.currentTimeMillis();
            Connection connection = ConnectionFactory.createConnection(conf);
            table = connection.getTable(TableName.valueOf(tableQualifier));
            long end = System.currentTimeMillis();
            LOG.info("create hbase table connection {} cost {}", tableQualifier, (end - start));
        } catch (Exception e) {
            LOG.error("Failed to create hbase table connection " + tableQualifier, e);
        }
        return table;
    }

    @Override
    public PooledObject<Table> wrap(Table table) {    
        return new DefaultPooledObject<>(table);
    }

    @Override
    public void destroyObject(PooledObject<Table> p) throws Exception {
        LOG.info("hbase table close");
        try {
            p.getObject().close();
        } catch (Exception e) {
            LOG.error("hbase close error", e);
        }
        
    }
}
