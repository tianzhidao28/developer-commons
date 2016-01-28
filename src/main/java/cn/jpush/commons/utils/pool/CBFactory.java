package cn.jpush.commons.utils.pool;

import cn.jpush.commons.utils.config.SystemConfig;
import com.couchbase.client.CouchbaseClient;
import com.couchbase.client.CouchbaseConnectionFactory;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

public class CBFactory extends BasePooledObjectFactory<CouchbaseClient> {
    
    private static Logger LOG = LoggerFactory.getLogger(CBFactory.class);

    private final String couchbaseName;
    private String serverAddress;
    private String bucket;
    private String pwd;
    private ArrayList<URI> serverList;

    public CBFactory(String couchbaseName) {
        this.couchbaseName = couchbaseName;
        initConfig();
    }

    @Override
    public CouchbaseClient create() throws Exception {
        return initCouchbaseClient();
    }

    @Override
    public PooledObject<CouchbaseClient> wrap(CouchbaseClient client) {
        return new DefaultPooledObject<CouchbaseClient>(client);
    }

    @Override
    public void destroyObject(PooledObject<CouchbaseClient> p) throws Exception {
        LOG.info("cb client shutdown");
        try {
            p.getObject().shutdown();
        } catch(Exception e) {
            LOG.error("cb shutdown error", e);
        }
    }

    private void initConfig() {
        serverAddress = SystemConfig.getProperty(couchbaseName + ".couchbase.host");
        bucket = SystemConfig.getProperty(couchbaseName + ".couchbase.bucket");
        pwd = SystemConfig.getProperty(couchbaseName + ".couchbase.pass");

        String[] serverNames = serverAddress.split(",");
        serverList = new ArrayList<URI>();
        for (String serverName : serverNames) {
            URI base = null;
            base = URI.create(String.format("http://%s/pools", serverName));
            serverList.add(base);
        }
    }

    private CouchbaseClient initCouchbaseClient() {
        long start = System.currentTimeMillis();

        CouchbaseClient client = null;
        try {
            client = new CouchbaseClient(new CouchbaseConnectionFactory(serverList, bucket, pwd));

        } catch (IOException e) {
            LOG.error(String.format("get CouchbaseClient Exception,host[%s],bucket[%s],pwd[%s].", serverAddress,
                    bucket, pwd));
        } catch (Exception e) {
            LOG.error("Init couchbase clien error " + couchbaseName, e);
        }
        long end = System.currentTimeMillis();
        LOG.info("init cb cost " + couchbaseName + " cost " + (end - start));
        return client;
    }
    
    
}
