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
package com.rocyuan.commons.utils;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rocyuan on 16/1/30.
 */
public class JSONUtilsTest {

    public static String jsonMapList = "";
    public static String jsonMapString = "";

    public static Map<String, java.lang.Object> map;

    public static List<Map<String, java.lang.Object>> lists;

    @BeforeClass
    public static void before() {


        map = new HashMap<>();
        map.put("age",22);
        map.put("name","rocyuan");
        map.put("hight",1.75);

        Map<String, java.lang.Object> map2 = new HashMap<>();
        map2.put("age",23);
        map2.put("name","yinxk");
        map2.put("hight",1.68);
        lists = new ArrayList<>();

        lists.add(map);
        lists.add(map2);

        jsonMapString = JSON.toJSONString(map);

        jsonMapList = JSON.toJSONString(lists);


    }

    @Test
    public void testGetJSONValueByTemplate() throws Exception {



    }

    @Test
    public void testParseJSON2Map() throws Exception {

        Map m = JSONUtils.parseJSON2Map(jsonMapString);

        Assert.assertEquals(m.get("age"),22);
        Assert.assertEquals(m.get("name"),"rocyuan");

        System.out.println("JSONUtils.parseJSON2Map test ok");

    }

    @Test
    public void testParseJSON2List() throws Exception {

        List<Map<String,Object>> list = JSONUtils.parseJSON2List(jsonMapList);
        Assert.assertTrue(list != null);
    }
}
