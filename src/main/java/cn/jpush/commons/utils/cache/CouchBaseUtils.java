package cn.jpush.commons.utils.cache;

import cn.jpush.commons.utils.exception.TimeoutException;
import com.couchbase.client.CouchbaseClient;
import net.spy.memcached.OperationTimeoutException;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * couchbase工具类，用于提供获取cb的client连接
 */
public class CouchBaseUtils {

	private static final Logger LOG = LoggerFactory.getLogger(CouchBaseUtils.class);

	/**
	 * 超时多少次进行断开重连
	 */
	private static final int TIMEOUT_TIMES_TO_RECONNECT = 20;
	/**
	 * 记录错误超时的调用次数
	 */
	private static ConcurrentHashMap<String,AtomicInteger> errCntsMap =  new ConcurrentHashMap<String,AtomicInteger>();

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
		}catch (OperationTimeoutException et){
			errorWork(couchBaseName);
		} catch(Exception e) {
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
		}catch (OperationTimeoutException et){
			errorWork(couchBaseName);
		} catch(Exception e) {
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






	/**
	 * 从CB中获取元素的值,中途出现异常则抛出
	 * @param couchBaseName
	 * @param key
	 * @return TimeoutException
	 */
	public static String getDataEx(String couchBaseName , String key) {

		String value = null;
		try {
			CouchbaseClient client = CouchBaseManager.getCouchbaseClientInstance(couchBaseName);
			value = ObjectUtils.toString(client.get(key));
			LOG.info("[CouchBase: {} ] get data ok, key={},value={}",couchBaseName, key, value);
		}catch (OperationTimeoutException et){
			errorWork(couchBaseName);
		} catch(Exception e) {
			LOG.error("[CouchBase: {} ] get data exception, key={},exception={}",couchBaseName, key,e.getMessage());
			throw new RuntimeException(e);
		}
		return value;
	}


	/**
	 * 从CB中获取元素的值,中途出现异常则抛出
	 * @param couchBaseName
	 * @param key
     * @return
     */
	public static Object getObjectDataEx(String couchBaseName , String key) {

		Object value = null;
		try {
			CouchbaseClient client = CouchBaseManager.getCouchbaseClientInstance(couchBaseName);
			value = client.get(key);
			LOG.info("[CouchBase: {} ] get data ok, key={},value={}",couchBaseName, key, value);
			return value;
		}catch (OperationTimeoutException et){
			errorWork(couchBaseName);
		} catch(Exception e) {
			LOG.error("[CouchBase: {} ] get data exception, key={},exception={}",couchBaseName, key,e.getMessage());
			throw new RuntimeException(e);
		}
		return value;
	}

	public static void errorWork(String couchBaseName) {
		LOG.error(" cb {} timeout",couchBaseName);
		AtomicInteger cnt = errCntsMap.get(couchBaseName);
		int num = cnt.incrementAndGet();
		if (num > TIMEOUT_TIMES_TO_RECONNECT) {

			CouchBaseManager.shutdown(couchBaseName);
			CouchBaseManager.initCouchbaseClientWithReTryAsync(couchBaseName,1);

			cnt.set(0);
			errCntsMap.put(couchBaseName,cnt);
			LOG.info("cb {} has reconnection... ",couchBaseName);
			throw new TimeoutException("CouchBase :" + couchBaseName + "操作超时");
		} else {

			errCntsMap.put(couchBaseName,cnt);
		}
	}

}
