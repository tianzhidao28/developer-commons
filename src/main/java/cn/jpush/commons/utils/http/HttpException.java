package cn.jpush.commons.utils.http;

import org.apache.http.HttpResponse;

/**
 * http错误的封装，不包括IO等异常
 * 提示性的message存放在message，非必须
 * status表示http状态码
 * response是附加的http响应对象，便于获取响应体的完整信息。
 * Created by leolin on 16/1/26.
 */
public class HttpException extends Exception{
    private Integer status;
    private HttpResponse response;

    public HttpException(HttpResponse response){
        super(
            String.format(
                "【HTTP Error】\n code:%d, reason:%s\n",
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase()
            )
        );
        this.status = response.getStatusLine().getStatusCode();
        this.response = response;
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
}
