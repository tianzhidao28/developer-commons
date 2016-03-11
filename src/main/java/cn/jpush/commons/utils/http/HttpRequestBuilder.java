package cn.jpush.commons.utils.http;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * post get put delete等方法的建造者
 * 链式调用的构造风格
 * 内容包括http request method、 params、 headers等
 * 支持RESTful，指定body为json的方式为：添加JSON_CONTENT_TYPE_WITH_UTF_8的header
 * Created by leolin on 16/1/25.
 */
public class HttpRequestBuilder {
    private String url = null;
    private List<NameValuePair> params = new LinkedList<>();
    private RequestType method = null;
    private List<Header> headers = new LinkedList<>();
    private String encode;
    public static Header JSON_CONTENT_TYPE_WITH_UTF_8 = new BasicHeader("Content-Type", "application/json; charset=utf-8");
    //默认contentType
    private String contentType="application/x-www-form-urlencoded";

    public enum RequestType {
        POST, GET, PUT, DELETE
    }

    private HttpRequestBuilder() {}

    public static HttpRequestBuilder getBuilder(RequestType method) {
        HttpRequestBuilder builder = new HttpRequestBuilder();
        builder.method = method;
        //set suggest default encode
        switch (method) {
            case POST:
            case PUT:
                builder.encode = "utf-8";
                break;

            case GET:
            case DELETE:
                builder.encode = "gbk";
                break;
        }
        return builder;
    }

    public HttpRequestBuilder addParam(List<NameValuePair> params) {
        for (NameValuePair item : params) {
            addParam(item);
        }
        return this;
    }

    public HttpRequestBuilder addParam(NameValuePair param) {
        params.add(param);
        return this;
    }

    public HttpRequestBuilder addParam(String name, String value) {
        params.add(new BasicNameValuePair(name, value));
        return this;
    }

    public HttpRequestBuilder addHeader(List<Header> headers) {
        for (Header header : headers) {
            addHeader(header);
        }
        return this;
    }

    public HttpRequestBuilder addHeader(Header header) {
        if("Content-Type".equals(header.getName() )){
            contentType = header.getValue();
        }
        this.headers.add(header);
        return this;
    }

    public HttpRequestBuilder addHeader(String key, String value) {
        this.headers.add(new BasicHeader(key, value));
        return this;
    }

    public HttpRequestBuilder basicAuth(String username, String password) {
        addHeader(
            new BasicHeader(
                HttpHeaders.AUTHORIZATION,
                "Basic " + Base64.encodeBase64String(
                    (username + ":" + password).getBytes()
                )
            )
        );
        return this;
    }

    public HttpRequestBuilder appkey(String appkey) {
        addHeader("X-App-Key", appkey);
        return this;
    }

    public HttpRequestBase build() {
        if (this.url == null)
            throw new RuntimeException("property 'url' must be not null");

        HttpRequestBase baseRequest = null;
        HttpEntityEnclosingRequestBase enclosingRequest = null;
        try {
            switch (this.method) {
                case POST:
                    enclosingRequest = new HttpPost(url);
                    break;
                case PUT:
                    enclosingRequest = new HttpPut(url);
                    break;

                case GET:
                    baseRequest = new HttpGet(urlJoinWithParams(url, params, encode));
                    break;
                case DELETE:
                    baseRequest = new HttpDelete(urlJoinWithParams(url, params, encode));
                    break;

            }

            if (enclosingRequest != null) {
                enclosingRequest.setEntity(getEnetity() );
                for (Header header : headers) {
                    enclosingRequest.addHeader(header);
                }
                return enclosingRequest;
            } else if (baseRequest != null) {

                for (Header header : headers) {
                    baseRequest.addHeader(header);
                }
                return baseRequest;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }


    //get delete等http请求方式的传参手段
    private static String urlJoinWithParams(String url, List<NameValuePair> params, String encode) {
        if (0 == params.size()) {
            return url;
        }
        return url + (url.contains("?") ? "&" : "?") + URLEncodedUtils.format(params, encode);
    }

    private StringEntity getEnetity(){
        try {
            if (contentType.contains("application/json")) {
                Map<String, String> data = new HashMap<>();
                for (NameValuePair param : params) {
                    data.put(param.getName(), param.getValue());
                }
                return new StringEntity(JSON.toJSONString(data), encode);
            } else {
                return new UrlEncodedFormEntity(params, encode);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpRequestBuilder setEncode(String encode) {
        this.encode = encode;
        return this;
    }

    public HttpRequestBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpRequestBuilder setMethod(RequestType method) {
        this.method = method;
        return this;
    }
}
