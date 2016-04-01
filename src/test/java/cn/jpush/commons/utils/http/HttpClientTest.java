package cn.jpush.commons.utils.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.client.HttpAsyncClient;
import org.junit.Test;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static cn.jpush.commons.utils.http.HttpRequestBuilder.RequestType.GET;
import static cn.jpush.commons.utils.http.HttpRequestBuilder.RequestType.POST;

/** 
* HttpClient Tester. 
* 
* @author ${USER}
* @since <pre>三月 11, 2016</pre> 
* @version 1.0 
*/ 
public class HttpClientTest { 

    public static class Person{
        private String name;
        private String sex;
        private Person p;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public Person getP() {
            return p;
        }

        public void setP(Person p) {
            this.p = p;
        }
    }

    @Test
    public void testRequest() throws HttpException, InterruptedException, IOException {
        String url = "http://www.baidu.com";
//        HttpClient httpClient = HttpClient.getHttpClient();
//        HttpRequestBase request = HttpRequestBuilder.getBuilder(HttpRequestBuilder.RequestType.POST)
//            .setUrl("https://api.im.jpush.cn/users")
//            .basicAuth("104a31bd8e8e582259fdf369", "21f6fae440d6f71223f5690c")
//            .setJsonBody("[{\n" +
//                "    \"username\": \"tjxsdfnj\",\n" +
//                "    \"password\": \"123456\"\n" +
//                "}]")
//            .addHeader(HttpRequestBuilder.JSON_CONTENT_TYPE_WITH_UTF_8).build();
//
//        Result result = httpClient.doRequest(request, "utf-8");
//        System.out.println(result.getBody());

        int loop = 100;
        CloseableHttpAsyncClient asyncClient = HttpAsyncClients.createDefault();
        asyncClient.start();
        while(loop -- != 0) {
            asyncClient.execute(
                HttpRequestBuilder.getBuilder(GET)
                    .setUrl("http://www.baidu.com")
                    .build(),
                new FutureCallback<HttpResponse>() {
                    @Override
                    public void completed(HttpResponse httpResponse) {
                        System.out.println(httpResponse.getStatusLine().getStatusCode());

                    }

                    @Override
                    public void failed(Exception e) {
                        System.out.println("cancelled");
                    }

                    @Override
                    public void cancelled() {
                        System.out.println("cancelled");
                    }
                }
            );
        }
//        asyncClient.close();
//        Thread.currentThread().join();
    }
} 
