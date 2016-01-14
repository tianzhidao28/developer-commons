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
        // RedisCacheManager.getInstanse("xx").llen("111");
    }
}