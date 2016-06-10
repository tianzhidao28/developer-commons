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


import cn.jpush.alarm.AlarmClient;
import com.rocyuan.commons.utils.config.SystemConfig;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.transport.TransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ESFactory extends BasePooledObjectFactory<Client> {

    private static Logger LOG = LoggerFactory.getLogger(ESFactory.class);

    private String clusterName = null;
    private static String[] esAddressList;


    static {
        esAddressList = SystemConfig.getPropertyArray("es.transport.ip");
    }

    public ESFactory() {
        this.clusterName = null;
    }

    public ESFactory(String clusterName) {
        this.clusterName = clusterName;
    }

    @Override
    public Client create() throws Exception {
        long start = System.currentTimeMillis();
        Client client = createClient();
        long end = System.currentTimeMillis();
        LOG.info("init es client cost " + (end - start));
        return client;
    }

    @Override
    public PooledObject<Client> wrap(Client client) {
        return new DefaultPooledObject<>(client);
    }

    @Override
    public void destroyObject(PooledObject<Client> p) throws Exception {
        LOG.info("close es client");
        try {
            p.getObject().close();
        } catch (Exception e) {
            LOG.error("destroy es client error", e);
            AlarmClient.send(84,String.format("[ES:clusterName:%s,esAddressList:%s] pool destroy ",clusterName,StringUtils.join(esAddressList)));
        }

    }

    private Client createClient() {
        TransportClient client = null;
        try {
            Settings settings = getSettings();
            TransportAddress[] addresses = getAddress();
            if(null == settings || null == addresses) {
                LOG.error("Setting not exist.");
                return null;
            }
            client = TransportClient.builder().settings(settings).build();
            client.addTransportAddresses(addresses);
        } catch (Exception e) {
            LOG.error("Failed to create client.", e);
            AlarmClient.send(84,"[ES] exception createClient"+e.getMessage());
        }
        return client;
    }

    private TransportAddress[] getAddress() {
        TransportAddress[] addresses = new TransportAddress[esAddressList.length];
        LOG.info("address size is " + esAddressList.length);
        for(int i = 0; i < esAddressList.length; i++) {
            String addr[] = esAddressList[i].split(":");
            try {
                addresses[i] = new InetSocketTransportAddress(InetAddress.getByName(addr[0]), Integer.valueOf(addr[1]));
                LOG.info("add " + esAddressList[i]);
            } catch (UnknownHostException e) {
                LOG.error("Failed to add address " + esAddressList[i], e);
                AlarmClient.send(84,String.format("[ES:(%s:%s)] UnknownHostException ",addr[0],addr[1]));

            }
        }
        return addresses;
    }

    private Settings getSettings() {
        if(null == clusterName) {
            Settings settings = Settings.settingsBuilder()
                    .put("es.client.transport.sniff", true)
                    .put("client.transport.ignore_cluster_name", true)
                    .build();
            return settings;
        } else {
            Settings settings = Settings.settingsBuilder()
                    .put("es.client.transport.sniff", true)
                    .put("cluster.name", clusterName)
                    .build();
            return settings;
        }
    }
}
