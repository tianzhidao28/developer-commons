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

import com.rocyuan.commons.utils.AlarmClient;
import com.rocyuan.commons.utils.io.SerializeUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class RedisCacheManager implements JedisCommands{
    private final Logger log = LoggerFactory.getLogger(RedisCacheManager.class);
    private static Map<String, RedisCacheManager> cache = new HashMap<String, RedisCacheManager>();
    private String poolName;

    /**
     * 构造函数
     *
     * @param poolName 队列名
     */
    private RedisCacheManager(String poolName) {
        super();
        this.poolName = poolName;
    }

    /**
     * 获取Redis缓存管理工具类的实例
     *
     * @param poolName 队列名
     */
    public static RedisCacheManager getInstanse(String poolName) {
        RedisCacheManager redisCacheManager = cache.get(poolName);
        if (cache.get(poolName) == null) {
            redisCacheManager = new RedisCacheManager(poolName);
            cache.put(poolName, redisCacheManager);
        }
        return redisCacheManager;
    }

    /**
     * 获取jedis连接
     */
    public synchronized Jedis getJedis() {
        try {
            Jedis resource = JedisFactory.getJedisInstance(poolName);
            if (resource != null) {
                return resource;
            } else {
                log.error(String.format("[Redis] get %s redis is empty.", poolName));
                return null;
            }
        } catch (Exception e) {
            log.error(String.format("[Redis] get %s redis exception", poolName), e);
            return null;
        }
    }


    public String set(final String key, final String value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.set(key, value);
            }
        }.getResult("set",key,value);
    }


    public String set(final byte[] key, final byte[] value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.set(key, value);
            }
        }.getResult("setbyte");
    }



    public String set(final String key, final String value, final String nxxx, final String expx, final long time) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.set(key, value, nxxx, expx, time);
            }
        }.getResult("set",key,value,nxxx,expx);
    }


    public String get(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.get(key);
            }
        }.getResult("get",key);
    }

    /**
     *

     */
    public Boolean exists(final String key) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.exists(key);
            }
        }.getResult("exists",key);
    }

    /**
     *

     */
    public Long del(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.del(key);
            }
        }.getResult("del",key);
    }

    /**
     *

     */
    public String type(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.type(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long expire(final String key, final int seconds) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.expire(key, seconds);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long expireAt(final String key, final long unixTime) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.expireAt(key, unixTime);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long ttl(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.ttl(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long move(final String key, final int dbIndex) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.move(key, dbIndex);
            }
        }.getResult();
    }

    /**
     *

     */
    public String getSet(final String key, final String value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.getSet(key, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long setnx(final String key, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.setnx(key, value);
            }
        }.getResult();
    }


    public String setex(final String key, final int seconds, final String value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.setex(key, seconds, value);
            }
        }.getResult("setex",key,seconds,value);
    }

    /**
     *

     */
    public Long decrBy(final String key, final long integer) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.decrBy(key, integer);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long decr(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.decr(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long incrBy(final String key, final long integer) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.incrBy(key, integer);
            }
        }.getResult();
    }

    /**
     *

     */
    public Double incrByFloat(final String key, final double value) {
        return new RedisExecutor<Double>() {
            @Override
            Double execute() {
                return jedis.incrByFloat(key, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long incr(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.incr(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long append(final String key, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.append(key, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public String substr(final String key, final int start, final int end) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.substr(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long hset(final String key, final String field, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hset(key, field, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public String hget(final String key, final String field) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.hget(key, field);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long hsetnx(final String key, final String field, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hsetnx(key, field, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public String hmset(final String key, final java.util.Map hash) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.hmset(key, hash);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List hmget(final String key, final String[] fields) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.hmget(key, fields);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long hincrBy(final String key, final String field, final long value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hincrBy(key, field, value);
            }
        }.getResult("hincrBy",key,field,value);
    }

    /**
     *

     */
    public Boolean hexists(final String key, final String field) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.hexists(key, field);
            }
        }.getResult("hexists",key,field);
    }

    /**
     *

     */
    public Long hdel(final String key, final String[] fields) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hdel(key, fields);
            }
        }.getResult("hdel",key,fields);
    }

    /**
     *

     */
    public Long hlen(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.hlen(key);
            }
        }.getResult("hlen",key);
    }

    /**
     *

     */
    public java.util.Set hkeys(final String key) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.hkeys(key);
            }
        }.getResult("hkeys",key);
    }

    /**
     *

     */
    public java.util.List hvals(final String key) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.hvals(key);
            }
        }.getResult("hvals",key);
    }

    /**
     *

     */
    public java.util.Map hgetAll(final String key) {
        return new RedisExecutor<java.util.Map>() {
            @Override
            java.util.Map execute() {
                return jedis.hgetAll(key);
            }
        }.getResult("hgetAll",key);
    }

    /**
     *

     */
    public Long rpush(final String key, final String[] strings) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.rpush(key, strings);
            }
        }.getResult("rpush",key,strings);
    }

    /**
     *

     */
    public Long lpush(final String key, final String[] strings) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.lpush(key, strings);
            }
        }.getResult("lpush",key,strings);
    }

    /**
     *

     */
    public Long llen(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.llen(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List lrange(final String key, final long start, final long end) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.lrange(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public String ltrim(final String key, final long start, final long end) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.ltrim(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public String lindex(final String key, final long index) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.lindex(key, index);
            }
        }.getResult();
    }

    /**
     *

     */
    public String lset(final String key, final long index, final String value) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.lset(key, index, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long lrem(final String key, final long count, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.lrem(key, count, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public String lpop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.lpop(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public String rpop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.rpop(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long sadd(final String key, final String[] members) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.sadd(key, members);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set smembers(final String key) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.smembers(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long srem(final String key, final String[] members) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.srem(key, members);
            }
        }.getResult();
    }

    /**
     *

     */
    public String spop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.spop(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set spop(final String key, final long count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.spop(key, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long scard(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.scard(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Boolean sismember(final String key, final String member) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.sismember(key, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public String srandmember(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.srandmember(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List srandmember(final String key, final int count) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.srandmember(key, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zadd(final String key, final double score, final String member) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zadd(key, score, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zadd(final String key, final java.util.Map scoreMembers) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zadd(key, scoreMembers);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrange(final String key, final long start, final long end) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrange(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zrem(final String key, final String[] members) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zrem(key, members);
            }
        }.getResult();
    }

    /**
     *

     */
    public Double zincrby(final String key, final double score, final String member) {
        return new RedisExecutor<Double>() {
            @Override
            Double execute() {
                return jedis.zincrby(key, score, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zrank(final String key, final String member) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zrank(key, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zrevrank(final String key, final String member) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zrevrank(key, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrange(final String key, final long start, final long end) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrange(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeWithScores(final String key, final long start, final long end) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeWithScores(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeWithScores(final String key, final long start, final long end) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeWithScores(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zcard(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zcard(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Double zscore(final String key, final String member) {
        return new RedisExecutor<Double>() {
            @Override
            Double execute() {
                return jedis.zscore(key, member);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List sort(final String key) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.sort(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List sort(final String key, final redis.clients.jedis.SortingParams sortingParameters) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.sort(key, sortingParameters);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List blpop(final String arg) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.blpop(arg);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.List brpop(final String arg) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.brpop(arg);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zcount(final String key, final double min, final double max) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zcount(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zcount(final String key, final String min, final String max) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zcount(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScore(final String key, final double min, final double max) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScore(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScore(final String key, final String min, final String max) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScore(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScore(final String key, final double min, final double max, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScore(final String key, final String min, final String max, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScore(key, min, max, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScoreWithScores(final String key, final double min, final double max) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScoreWithScores(final String key, final String min, final String max) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScoreWithScores(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScoreWithScores(final String key, final double min, final double max, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByScoreWithScores(final String key, final String min, final String max, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByScoreWithScores(key, min, max, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScore(final String key, final double max, final double min) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScore(key, max, min);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScore(final String key, final String max, final String min) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScore(key, max, min);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScore(final String key, final double max, final double min, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScoreWithScores(final String key, final double max, final double min) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScoreWithScores(final String key, final double max, final double min, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScoreWithScores(final String key, final String max, final String min, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScoreWithScores(key, max, min, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScore(final String key, final String max, final String min, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScore(key, max, min, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByScoreWithScores(final String key, final String max, final String min) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByScoreWithScores(key, max, min);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zremrangeByRank(final String key, final long start, final long end) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zremrangeByRank(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zremrangeByScore(final String key, final double start, final double end) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zremrangeByScore(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zremrangeByScore(final String key, final String start, final String end) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zremrangeByScore(key, start, end);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long zlexcount(final String key, final String min, final String max) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zlexcount(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByLex(final String key, final String min, final String max) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByLex(key, min, max);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrangeByLex(final String key, final String min, final String max, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrangeByLex(key, min, max, offset, count);
            }
        }.getResult();
    }

    /**
     *

     */
    public java.util.Set zrevrangeByLex(final String key, final String max, final String min) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByLex(key, max, min);
            }
        }.getResult("zrevrangeByLex",key,max,min);
    }

    /**
     *

     */
    public java.util.Set zrevrangeByLex(final String key, final String max, final String min, final int offset, final int count) {
        return new RedisExecutor<java.util.Set>() {
            @Override
            java.util.Set execute() {
                return jedis.zrevrangeByLex(key, max, min, offset, count);
            }
        }.getResult("zrevrangeByLex",key,max,min,offset,count);
    }

    /**
     *

     */
    public Long zremrangeByLex(final String key, final String min, final String max) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.zremrangeByLex(key, min, max);
            }
        }.getResult("zremrangeByLex",key,min,max);
    }

    /**
     *

     */
    public Long strlen(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.strlen(key);
            }
        }.getResult("strlen",key);
    }

    /**
     *

     */
    public Long lpushx(final String key, final String[] string) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.lpushx(key, string);
            }
        }.getResult("lpushx",key,string);
    }

    /**
     *

     */
    public Long persist(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.persist(key);
            }
        }.getResult("persist",key);
    }

    /**
     *

     */
    public Long rpushx(final String key, final String[] string) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.rpushx(key, string);
            }
        }.getResult("rpushx",key,string);
    }

    /**
     *

     */
    public String echo(final String string) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.echo(string);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long linsert(final String key, final redis.clients.jedis.BinaryClient.LIST_POSITION where, final String pivot, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.linsert(key, where, pivot, value);
            }
        }.getResult("linsert",key,where,pivot,value);
    }

    /**
     *

     */
    public Boolean setbit(final String key, final long offset, final boolean value) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.setbit(key, offset, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public Boolean setbit(final String key, final long offset, final String value) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.setbit(key, offset, value);
            }
        }.getResult();
    }

    /**
     *

     */
    public Boolean getbit(final String key, final long offset) {
        return new RedisExecutor<Boolean>() {
            @Override
            Boolean execute() {
                return jedis.getbit(key, offset);
            }
        }.getResult("getbit",key,offset);
    }

    /**
     *

     */
    public Long setrange(final String key, final long offset, final String value) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.setrange(key, offset, value);
            }
        }.getResult("setrange",key,offset,value);
    }

    /**
     *

     */
    public String getrange(final String key, final long startOffset, final long endOffset) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.getrange(key, startOffset, endOffset);
            }
        }.getResult("getrange",key,startOffset,endOffset);
    }

    /**
     *

     */
    public Long bitcount(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.bitcount(key);
            }
        }.getResult();
    }

    /**
     *

     */
    public Long bitcount(final String key, final long start, final long end) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.bitcount(key, start, end);
            }
        }.getResult("bitcount",key,start,end);
    }

    /**
     *

     */
    public Long pexpire(final String key, final long milliseconds) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.pexpire(key, milliseconds);
            }
        }.getResult("pexpire",key,milliseconds);
    }

    /**
     *

     */
    public Long pexpireAt(final String key, final long millisecondsTimestamp) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.pexpireAt(key, millisecondsTimestamp);
            }
        }.getResult("pexpireAt",key,millisecondsTimestamp);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult hscan(final String key, final int cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.hscan(key, cursor);
            }
        }.getResult("hscan",key,cursor);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult sscan(final String key, final int cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.sscan(key, cursor);
            }
        }.getResult("sscan",key,cursor);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult zscan(final String key, final int cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.zscan(key, cursor);
            }
        }.getResult("zscan",key,cursor);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult hscan(final String key, final String cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.hscan(key, cursor);
            }
        }.getResult("hscan",key,cursor);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult sscan(final String key, final String cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.sscan(key, cursor);
            }
        }.getResult("sscan",key,cursor);
    }

    /**
     *

     */
    public redis.clients.jedis.ScanResult zscan(final String key, final String cursor) {
        return new RedisExecutor<redis.clients.jedis.ScanResult>() {
            @Override
            redis.clients.jedis.ScanResult execute() {
                return jedis.zscan(key, cursor);
            }
        }.getResult("zscan",key,cursor);
    }

    /**
     *

     */
    public Long pfadd(final String key, final String[] elements) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.pfadd(key, elements);
            }
        }.getResult("pfadd",key,elements);
    }

    /**
     *

     */
    public long pfcount(final String key) {
        return new RedisExecutor<Long>() {
            @Override
            Long execute() {
                return jedis.pfcount(key);
            }
        }.getResult("pfcount",key);
    }

    /**
     *

     */
    public java.util.List blpop(final int timeout, final String key) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.blpop(timeout, key);
            }
        }.getResult("blpop",timeout,key);
    }

    /**
     *

     */
    public java.util.List brpop(final int timeout, final String key) {
        return new RedisExecutor<java.util.List>() {
            @Override
            java.util.List execute() {
                return jedis.brpop(timeout, key);
            }
        }.getResult("brpop",timeout,key);
    }

    /**
     * 判断链接jedis对象是否为空
     */
    public boolean isNullRedis() {
        return getJedis() == null;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis 待释放的jedis对象
     */
    public void releaseRedis(Jedis jedis) {
        if (jedis != null) {
            JedisFactory.release(poolName, jedis);
        }
    }

    /**
     * 有序优先级队列入队
     *
     * @param key   键(队列名)
     * @param value 值
     * @param score 优先级
     */
    public void sortedPriorityPush(final String key, final String value, final double score) {
        new RedisExecutor<Long>() {
            @Override
            Long execute() {
                Long result = jedis.zadd(key, score, value);
                log.info("[Redis({})] push data key = {},value = {} success ;return {}", poolName,
                    key, value, result);
                return result;
            }
        }.getResult();

    }

    /**
     * 有序优先级队列出队
     *
     * @param key 键(队列名)
     */
    public String sortedPriorityPop(final String key) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                Set<String> set = jedis.zrange(key, 0, 0);
                if (set.size() > 0) {
                    String value = set.iterator().next();
                    jedis.zrem(key, value);
                    return value;
                }
                return null;
            }
        }.getResult();

    }

    /**
     * jdk 序列化set
     * @param key key.getBytes()
     * @param seconds
     * @param value 序列化
     * @return
     */
    public String setObjectEx(final String key,final int seconds, final Object value ) {
        return new RedisExecutor<String>() {
            @Override
            String execute() {
                return jedis.setex(key.getBytes(), seconds , SerializeUtils.serialize(value));
            }
        }.getResult();
    }


    /**
     * 获取序列化存储的值
     * @param key
     * @return
     */
    public Object getObject(final String key) {
        return new RedisExecutor<Object>() {
            @Override
            Object execute() {
                return SerializeUtils.deserialize(jedis.get(key.getBytes()));
            }
        }.getResult();
    }

    abstract class RedisExecutor<T> {
        Jedis jedis;

        public RedisExecutor() {
            jedis = getJedis();
        }

        abstract T execute();

        /**
         * 调用{@link #execute()}并返回执行结果
         * 它保证在执行{@link #execute()}之后释放数据源returnResource(jedis)
         *
         * @return 执行结果
         */
        public T getResult() {
            T result = null;
            try {
                result = execute();
            } catch (JedisConnectionException e) {
                JedisFactory.realeaseBroken(poolName, jedis);
                log.error("[Redis][poolName = {}] Connection异常 ", poolName, e);
                AlarmClient.send(84,String.format("[redis:%s]JedisConnectionException:%s",poolName,e.getMessage()));
            } catch (Throwable e) {
                JedisFactory.realeaseBroken(poolName, jedis);
                log.error("[Redis][poolName = {}] 执行命令时异常 ", poolName, e);
                AlarmClient.send(84,String.format("[redis:%s]Jedis操作异常:%s",poolName,e.getMessage()));

            }finally {
                JedisFactory.release(poolName, jedis);
            }
            return result;
        }



        /**
         * 打印出执行参数
         * @return 执行结果
         */
        public T getResult(String command,Object ...params) {
            T result = null;
            try {
                result = execute();
            } catch (JedisConnectionException e) {
                JedisFactory.realeaseBroken(poolName, jedis);
                log.error("[Redis][poolName = {}] Connection异常 ", poolName, e);
                AlarmClient.send(84,String.format("[redis:%s] %s(%s) ConnectionException:%s",poolName,command, ToStringBuilder.reflectionToString(params),e.getMessage()));
            } catch (Throwable e) {
                JedisFactory.realeaseBroken(poolName, jedis);
                log.error("[Redis][poolName = {}] 执行命令时异常 ", poolName, e);
                AlarmClient.send(84,String.format("[redis:%s]操作异常 %s(%s) :%s",poolName,command, ToStringBuilder.reflectionToString(params),e.getMessage()));
            }finally {
                JedisFactory.release(poolName, jedis);
            }
            return result;
        }


    }


}
