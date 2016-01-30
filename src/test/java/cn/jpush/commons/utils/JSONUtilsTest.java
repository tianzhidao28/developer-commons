package cn.jpush.commons.utils;

import com.alibaba.fastjson.JSON;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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