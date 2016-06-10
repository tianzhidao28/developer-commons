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
package com.rocyuan.commons.utils.cache;



import com.rocyuan.commons.utils.config.SystemConfig;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import com.couchbase.client.CouchbaseConnectionFactoryBuilder;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.ConnectionObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by rocyuan on 2015/10/23.
 */

public class CouchBaseManager {
    private static Logger LOG = LoggerFactory.getLogger(CouchBaseManager.class);
    private static CouchbaseClient client = null;
    private static Map<String, CouchbaseClient> couchbaseClientMap = new HashMap<String, CouchbaseClient>();


    /**
     * 失败的话 就重连
     * @param timesNum 重连的次数
     */
    public static synchronized CouchbaseClient initCouchbaseClientWithReTry(final String couchbaseName , int timesNum) {

        for ( int times = 0 ; times < timesNum ; ++times ) {
            CouchbaseClient client = initCouchbaseClient(couchbaseName);
            if (client != null) {
                LOG.info(" Couchbase[{}{} times] connect OK !",couchbaseName,times);
                client.addObserver(new ConnectionObserver() {
                    @Override
                    public void connectionEstablished(SocketAddress socketAddress, int i) {
                        LOG.info("Couchbase[{},{}] reconnected ",couchbaseName,socketAddress.toString());
                    }

                    @Override
                    public void connectionLost(SocketAddress socketAddress) {
                        LOG.warn(" Couchbase[{},{}] lost ",couchbaseName,socketAddress.toString());
                    }
                });
                return client;
            }
            continue;
        }
        LOG.error("Couchbase[{}{} times] 连不上!!!",couchbaseName,timesNum);
        return null;

    }

    public static void initCouchbaseClientWithReTryAsync(final String couchbaseName , final int timesNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                initCouchbaseClientWithReTry(couchbaseName,timesNum);
            }
        }).start();
    }


    public static CouchbaseClient initCouchbaseClient(final String couchbaseName) {
        String serverAddress = SystemConfig.getProperty(couchbaseName
                + ".couchbase.host");
        String bucket = SystemConfig.getProperty(couchbaseName
                + ".couchbase.bucket");
        String pwd = SystemConfig
                .getProperty(couchbaseName + ".couchbase.pass");

        String[] serverNames = serverAddress.split(",");
        ArrayList<URI> serverList = new ArrayList<URI>();
        for (String serverName : serverNames) {
            URI base = null;
            base = URI.create(String.format("http://%s/pools", serverName));
            serverList.add(base);
        }

        try {
            CouchbaseConnectionFactoryBuilder ccfb = new CouchbaseConnectionFactoryBuilder();
            ccfb.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY);
            ccfb.setOpTimeout(10000);
            ccfb.setOpQueueMaxBlockTime(5000);
            ccfb.setMaxReconnectDelay(1500);
            CouchbaseConnectionFactory cf = ccfb.buildCouchbaseConnection(
                    serverList, bucket, pwd);
            client = new CouchbaseClient(cf);
            client.addObserver(new ConnectionObserver() {
                @Override
                public void connectionEstablished(SocketAddress socketAddress, int i) {
                    LOG.info("Couchbase[{},{}] reconnected ",couchbaseName,socketAddress.toString());
                }

                @Override
                public void connectionLost(SocketAddress socketAddress) {
                    LOG.warn(" Couchbase[{},{}] lost ",couchbaseName,socketAddress.toString());
                }
            });
        } catch (IOException e) {
            LOG.error(String
                    .format("get CouchbaseClient Exception,host[%s],bucket[%s],pwd[%s].",
                            serverAddress, bucket, pwd));
            return null;
        } catch (Exception e) {
            LOG.error(
                    String.format(
                            "get CouchbaseClient Exception,host[%s],bucket[%s],pwd[%s].",
                            serverAddress, bucket, pwd), e);
            return null;
        }

        couchbaseClientMap.put(couchbaseName, client);
        return client;
    }

    public synchronized static CouchbaseClient getCouchbaseClientInstance(
            String couchbaseName) {
        if (couchbaseClientMap.containsKey(couchbaseName))
            return couchbaseClientMap.get(couchbaseName);
        return initCouchbaseClient(couchbaseName);
    }

    public static void shutdown(String couchbaseName) {
        CouchbaseClient client = couchbaseClientMap.get(couchbaseName);
        if(client != null) {
            try{
                client.shutdown(1, TimeUnit.MINUTES);
                couchbaseClientMap.remove(couchbaseName);
            }catch (Exception e){
                LOG.error("close counchbase client exception,counchbaseName={} ",couchbaseName);
            }

        }

    }



}

