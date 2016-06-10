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

import org.junit.Test;

/**
 * Created by rocyuan on 16/1/14.
 */
public class RedisCacheManagerTest {

    @Test
    public void testLlen() throws Exception {
        // 自动打开连接  关闭连接  不适合用在高并发的地方
        RedisCacheManager.getInstanse("phone-redis").set("222", "key");
        RedisCacheManager.getInstanse("phone-redis").set("222", "key2");

        RedisCacheManager.getInstanse("phone-redis").lpush(null,new String[]{"appkey1212_test","appkey12123_test2"});
        System.out.println(RedisCacheManager.getInstanse("phone-redis").get("222"));
    }
}
