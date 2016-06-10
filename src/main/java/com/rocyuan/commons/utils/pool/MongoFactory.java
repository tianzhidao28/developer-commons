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


import com.rocyuan.commons.utils.config.SystemConfig;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class MongoFactory extends BasePooledObjectFactory<MongoClient> {

    private static final Logger LOG = LoggerFactory.getLogger(MongoFactory.class);

    private static List<ServerAddress> serverAddresses;
    private static List<MongoCredential> credentials;

    static {
        String[] hosts = SystemConfig.getPropertyArray("mongodb.host");
        String userName = SystemConfig.getProperty("mongodb.user");
        String password = SystemConfig.getProperty("mongodb.pass");
        String database = SystemConfig.getProperty("mongodb.db");
        serverAddresses = new ArrayList<>(hosts.length);
        for(int i = 0; i < hosts.length; i++) {
            String[] hostPort = hosts[i].split(":");
            serverAddresses.add(new ServerAddress(hostPort[0], Integer.valueOf(hostPort[1])));
        }
        credentials = new ArrayList<>();
        credentials.add(MongoCredential.createCredential(userName, database, password.toCharArray()));
    }

    @Override
    public MongoClient create() throws Exception {
        MongoClient client = null;
        try{
            client= new MongoClient(serverAddresses, credentials);
        } catch (Exception e) {
            LOG.error("Failed to create mongodb client.", e);
        }
        return client;
    }

    @Override
    public PooledObject<MongoClient> wrap(MongoClient db) {
        return new DefaultPooledObject<>(db);
    }

    @Override
    public void destroyObject(PooledObject<MongoClient> p) throws Exception {
        try{
            p.getObject().close();
        } catch (Exception e) {
            LOG.error("Failed to destroy mongodb client object", e);
        }
    }
}
