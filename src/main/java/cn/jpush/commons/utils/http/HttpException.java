package cn.jpush.commons.utils.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http错误的封装，不包括IO等异常
 * 提示性的message存放在message，非必须
 * status表示http状态码
 * response是附加的http响应对象，便于获取响应体的完整信息。
 * Created by leolin on 16/1/26.
 */
public class HttpException extends Exception implements Result {
    private Integer status;
    private HttpResponse response;
    private String decode;
    private String body;

    public HttpException(HttpResponse response, String decode){
        super(
            String.format(
                "【HTTP Error】\n code:%d, reason:%s\n",
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase()
            )
        );
        this.status = response.getStatusLine().getStatusCode();
        this.response = response;
        this.decode = decode;
        try {
            body = EntityUtils.toString(this.response.getEntity(), this.decode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    /**
     * get response body
     * @return response body
     */
    public String getBody() {
        return this.body;
    }

    /**
     * get http header
     * @return http headers
     */
    public Header[] getHeaders() {
        return response.getAllHeaders();
    }
}
