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
package com.rocyuan.commons.utils.cache.redis;



import com.rocyuan.commons.utils.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Jedis工厂类
 */
public class JedisFactory {
	private static final Logger LOG = LoggerFactory.getLogger(JedisFactory.class);
	private final static int MAX_ACTIVE = 2000;
	private final static int MAX_IDLE = 800;
	private final static int MAX_WAIT = 10000;
	private final static int TIMEOUT = 6000;

	private static Map<String, JedisPool> jedisPools = new HashMap<String, JedisPool>();

	/**
	 * 初始化jedis连接池
	 * @param jedisName jedis名称
	 * @return jedis连接池
	 */
	public static JedisPool initJedisPool(String jedisName) {
		JedisPool jPool = jedisPools.get(jedisName);
		if (jPool == null) {
			String host = SystemConfig.getProperty(jedisName + ".redis.host");
			int port = SystemConfig.getIntProperty(jedisName + ".redis.port");
			jPool = newJeisPool(host, port);
			jedisPools.put(jedisName, jPool);
		}
		return jPool;
	}
	/**
	 * 获取jedis对象实例
	 * @param jedisName jedis名称
	 * @return jedis对象
	 */
	public static Jedis getJedisInstance(String jedisName) {
		LOG.debug("get jedis[name=" + jedisName + "]");
		JedisPool jedisPool = jedisPools.get(jedisName);
		if (jedisPool == null) {
			jedisPool = initJedisPool(jedisName);
		}

		Jedis jedis = null;
		for (int i = 0; i < 4; i++) {
			try {
				jedis = jedisPool.getResource();
				break;
			} catch (Exception e) {
				LOG.error("get resource from jedis pool error. times "
						+ (i + 1) + ". retry...", e);
				jedisPool.returnBrokenResource(jedis);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					LOG.warn("sleep error", e1);
				}
			}
		}
		return jedis;
	}
	/**
	 * 创建一个新的jedis连接池
	 * @param host 主机地址
	 * @param port 端口号
	 * @return jedis连接池
	 */
	private static JedisPool newJeisPool(String host, int port) {
		LOG.info("init jedis pool[" + host + ":" + port + "]");
		JedisPoolConfig config = new JedisPoolConfig();
		config.setTestOnReturn(false);
		config.setTestOnBorrow(false);
		config.setMaxTotal(MAX_ACTIVE);
		config.setMaxIdle(MAX_IDLE);
		config.setMaxWaitMillis(MAX_WAIT);
		return new JedisPool(config, host, port, TIMEOUT);
	}

	/**
	 * 配合使用getJedisInstance方法后将jedis对象释放回连接池中
	 * @param jedis 使用完毕的Jedis对象
	 * @return true 释放成功；否则返回false
	 */
	public static boolean release(String poolName, Jedis jedis) {
		LOG.debug("release jedis pool[name=" + poolName + "]");

		JedisPool jedisPool = jedisPools.get(poolName);
		if (jedisPool != null && jedis != null) {
			try {
				jedisPool.returnResource(jedis);
			} catch (Exception e) {
				jedisPool.returnBrokenResource(jedis);
			}
			return true;
		}
		return false;
	}

	public static void realeaseBroken(String poolName, Jedis jedis) {
		JedisPool jedisPool = jedisPools.get(poolName);
		if(jedisPool!=null) {
			jedisPool.returnBrokenResource(jedis);
		}
	}


	/**
	 * 将jedis连接池中的所有jedis连接全部销毁
	 */
	public static void destroy() {
		LOG.debug("destroy all pool");
		for (Iterator<JedisPool> itors = jedisPools.values().iterator(); itors
				.hasNext();) {
			try {
				JedisPool jedisPool = itors.next();
				jedisPool.destroy();
			} finally {
			}
		}
	}
	/**
	 * 指定销毁具体的一个jedis连接池
	 * @param poolName 连接池名称
	 */
	public static void destroy(String poolName) {
		try {
			jedisPools.get(poolName).destroy();
		} catch (Exception e) {
			LOG.warn("destory redis pool[" + poolName + "] error", e);
		}
	}
}
