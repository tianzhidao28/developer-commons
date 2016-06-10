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
package com.rocyuan.commons.utils.http;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.junit.Test;

import java.io.IOException;

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
                HttpRequestBuilder.getBuilder(HttpRequestBuilder.RequestType.GET)
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
