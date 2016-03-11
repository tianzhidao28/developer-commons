package cn.jpush.commons.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.CookieStore;
import java.util.Objects;
import java.util.Set;

/**
 * 高效的http工具
 * 主要通过HttpRequestBuilder生成请求体，然后交给HttpClient做请求
 * 示例
 *   String url = "http://183.232.42.208:8080" + "/token";
 *   String charset = "utf-8";
 *   HttpClient httpClient = HttpClient.getHttpClient();
 *   HttpRequestBase request = new HttpRequestBuilder(HttpRequestBuilder.RequestType.GET)
 *       .setUrl(url)
 *       .addHeader(HttpRequestBuilder.JSON_CONTENT_TYPE_WITH_UTF_8)
 *       .basicAuth("13178770", "222222")
 *       .build();
 *   String result = httpClient.doRequest(request, "utf-8");
 *  Created by leolin on 16/1/25.
 */
public class HttpClient {
    private DefaultHttpClient client;

    private HttpClient(){}

    public static HttpClient getHttpClient(){
        HttpClient httpClient = new HttpClient();
        httpClient.client = new DefaultHttpClient();
        return httpClient;
    }

    public static HttpClient getHttpsClient(){
        HttpClient httpClient = new HttpClient();
        httpClient.client = new SSLClient();
        return httpClient;
    }

    /**
     * @param httpRequest    请求体
     * @param decode         body的解码
     * @param expectStatuses 期望的http返回码，不符合期望的时候抛出HttpException异常
     * @return 响应体
     */
    public String doRequest(HttpRequestBase httpRequest, String decode, Set<Integer> expectStatuses) throws HttpException {
        String result = null;
        try {
            HttpResponse response = client.execute(httpRequest);
            if (response != null) {
                HttpEntity resEntity = response.getEntity();
                if (expectStatuses != null && !expectStatuses.contains(response.getStatusLine().getStatusCode())) {
                    throw new HttpException(response);
                }
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, decode);
                }
            }
        } catch (HttpException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public  String doRequest(HttpRequestBase request, String decode) {
        try {
            return doRequest(request, decode, null);
        } catch (HttpException e) {
            return null;
        }
    }

    public org.apache.http.client.CookieStore getCookies(){
        return client.getCookieStore();
    }

    public void setCookies(org.apache.http.client.CookieStore cookies){
        client.setCookieStore(cookies);
    }
}