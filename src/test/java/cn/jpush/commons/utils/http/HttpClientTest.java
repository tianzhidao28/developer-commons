package cn.jpush.commons.utils.http; 

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After; 

/** 
* HttpClient Tester. 
* 
* @author ${USER}
* @since <pre>三月 11, 2016</pre> 
* @version 1.0 
*/ 
public class HttpClientTest { 


    @Test
    public void testRequest(){
        String url = "http://www.baidu.com";
        HttpClient httpClient = HttpClient.getHttpClient();
        HttpRequestBase request = HttpRequestBuilder.getBuilder(HttpRequestBuilder.RequestType.GET)
            .setUrl(url)
            .build();
        String result = httpClient.doRequest(request, "utf-8");
        System.out.println(result);
    }
} 
