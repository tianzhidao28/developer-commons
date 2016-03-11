package cn.jpush.commons.utils.cache; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* CouchBaseUtils Tester. 
* 
* @author ${USER}
* @since <pre>三月 11, 2016</pre> 
* @version 1.0 
*/ 
public class CouchBaseUtilsTest {
    @Test
    public void simpleTest(){
        CouchBaseUtils.setData("app_cb_cache_all", "key", "value");
        System.out.println(CouchBaseUtils.getData("app_cb_cache_all", "key"));
        CouchBaseUtils.delData("app_cb_cache_all", "key");
    }
}
