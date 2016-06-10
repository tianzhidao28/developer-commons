/**
 *
 * Copyright (c) 2016, rocyuan, admin@rocyuan.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rocyuan.commons.utils.pool;

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

public class HBaseFactory extends BasePooledObjectFactory<Table> {

    private static Logger LOG = LoggerFactory.getLogger(HBaseFactory.class);

    private static Configuration conf = HBaseConfiguration.create();

    private final String tableQualifier;
    private static Connection connection;

    public HBaseFactory(String tableQualifier) {
        this.tableQualifier = tableQualifier;
    }

    static {
        initConnection();
    }

    public static void initConnection() {
        try {
            conf = HBaseConfiguration.create();
            long start = System.currentTimeMillis();
            connection = ConnectionFactory.createConnection(conf);
            long end = System.currentTimeMillis();
            LOG.info("init hbase connection cost " + (end - start));
        } catch (Exception e) {
            LOG.error("hbase create connection error", e);
        }
    }

    @Override
    public Table create() throws Exception {
        Table table = null;
        try {
            long start = System.currentTimeMillis();
//            Connection connection = ConnectionFactory.createConnection(conf);
            if (connection == null || connection.isAborted() || connection.isClosed()) {
                initConnection();
            }
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
