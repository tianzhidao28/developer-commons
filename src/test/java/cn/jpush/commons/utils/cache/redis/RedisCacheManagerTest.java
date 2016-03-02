package cn.jpush.commons.utils.cache.redis;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by rocyuan on 16/1/14.
 */
public class RedisCacheManagerTest {

    @Test
    public void testLlen() throws Exception {
        // 自动打开连接  关闭连接  不适合用在高并发的地方
<<<<<<< Updated upstream
        RedisCacheManager.getInstanse("phone-redis").del("222");
        RedisCacheManager.getInstanse("phone-redis").hset("222", "key", "123");
        System.out.println(RedisCacheManager.getInstanse("phone-redis").hexists("222","key"));
=======
        RedisCacheManager.getInstanse("phone-redis").set("222", "key");
        RedisCacheManager.getInstanse("phone-redis").set("222", "key2");
        System.out.println(RedisCacheManager.getInstanse("phone-redis").get("222"));
        System.out.println(String.valueOf(null));
>>>>>>> Stashed changes
    }
}