package cn.jpush.commons.utils.pool;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DBPool {


	
	private static Logger LOG = LoggerFactory.getLogger(DBPool.class);
	private static Map<String, ComboPooledDataSource> poolMap = new HashMap<>();

	static {
		System.setProperty("com.mchange.v2.c3p0.cfg.xml",
				System.getProperty("user.dir") + "/c3p0-config.xml");
	}

	public  static void register(String key) {
		try {
			ComboPooledDataSource statCpds = new ComboPooledDataSource(key);
			poolMap.put(key, statCpds);
		} catch (Exception e) {
			LOG.error("Failed to register db " + key, e);
		}
	}

	public  static Connection getConnection(String key) {
		try {
			ComboPooledDataSource cpds = null;
			synchronized (poolMap) {
                if (poolMap.containsKey(key)) {
                    cpds = poolMap.get(key);
                } else {
                    cpds = new ComboPooledDataSource(key);
                    poolMap.put(key, cpds);
                }
            }
			Connection conn = cpds.getConnection();
			return conn;
		} catch (SQLException e) {
			LOG.error("get connection error", e);
			return null;
		}
	}

	public static void shutdown() {
		Iterator<ComboPooledDataSource> itors = poolMap.values().iterator();
		while (itors.hasNext()) {
			ComboPooledDataSource cpds = itors.next();
			cpds.close();
		}
	}
	
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
				conn = null;
			} catch (Exception e) {
				LOG.error("close conn error", e);
			}
		}
	}
}
