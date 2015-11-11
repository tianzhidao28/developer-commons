package cn.jpush.commons.utils.cache;

import com.couchbase.client.CouchbaseClient;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * couchbase工具类，用于提供获取cb的client连接
 */
public class CouchBaseUtils {
	private static final Logger LOG = LoggerFactory.getLogger(CouchBaseUtils.class);

	/**
	 * 从CB中获取元素的值
	 * @param couchBaseName
	 * @param key
	 * @return
	 */
	public static String getData(String couchBaseName , String key) {

		String value = null;
		try {
			CouchbaseClient client = CouchBaseManager.getCouchbaseClientInstance(couchBaseName);
			value = ObjectUtils.toString(client.get(key));
			LOG.info("[CouchBase: {} ] get data ok, key={},value={}",couchBaseName, key, value);
		}catch(Exception e) {
			LOG.error("[CouchBase: {} ] get data exception, key={},exception={}",couchBaseName, key,e.getMessage());
		}
		return value;
	}


	public static Object getObjectData(String couchBaseName , String key) {

		Object value = null;
		try {
			CouchbaseClient client = CouchBaseManager.getCouchbaseClientInstance(couchBaseName);
			value = client.get(key);
			LOG.info("[CouchBase: {} ] get data ok, key={},value={}",couchBaseName, key, value);
			return value;
		}catch(Exception e) {
			LOG.error("[CouchBase: {} ] get data exception, key={},exception={}",couchBaseName, key,e.getMessage());
		}
		return value;
	}


	public static void setData(String couchBaseName, String key, String value) {
		try {
			CouchbaseClient client = CouchBaseManager.getCouchbaseClientInstance(couchBaseName);
			client.set(key, 0, value);
			LOG.info("[CouchBase: {} ] set data ok, key={},value={}",couchBaseName, key, value);
		} catch (Exception e) {
			LOG.error("[CouchBase: {} ] set data exeception, key={},exeception={}",couchBaseName, key, e.getMessage());
		}
	}

}
