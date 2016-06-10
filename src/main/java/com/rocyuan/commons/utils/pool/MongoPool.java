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
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoPool {

    private static final Logger LOG = LoggerFactory.getLogger(MongoPool.class);

    private static GenericObjectPoolConfig config = null;
    private static ObjectPool<MongoClient> pool;

    static {
        config = new GenericObjectPoolConfig();

        config.setMaxTotal(SystemConfig.getIntProperty("mongodb.max.total",3000));
        config.setMaxIdle(SystemConfig.getIntProperty("mongodb.max.idle",100));
        config.setMinIdle(SystemConfig.getIntProperty("mongodb.min.idle",3));

        config.setTestWhileIdle(true);
        config.setMinEvictableIdleTimeMillis(120000L);
        config.setTimeBetweenEvictionRunsMillis(60000L);

        pool = new GenericObjectPool<>(new MongoFactory(), config);

    }

    public static MongoClient getClient() {
        try {
            return pool.borrowObject();
        } catch (Exception e) {
            LOG.error("Failed to get mongodb client", e);
        }
        return null;
    }

    public static void returnClient(MongoClient client) {
        try {
            pool.returnObject(client);
        } catch (Exception e) {
            LOG.error("Failed to return mongodb client.", e);
        }
    }

    public static void destroyClient(MongoClient client) {
        try {
            pool.invalidateObject(client);
        } catch (Exception e) {
            LOG.error("Failed to destroy mongodb client", e);
        }
    }
}
