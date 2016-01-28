package cn.jpush.commons.utils.pool;


import cn.jpush.commons.utils.config.SystemConfig;
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
