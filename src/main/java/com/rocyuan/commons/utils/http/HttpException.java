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

import org.apache.http.Header;
import org.apache.http.HttpResponse;

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
    private Result result;

    public HttpException(HttpResponse response, String decode){
        super(
            String.format(
                "【HTTP Error】\n code:%d, reason:%s\n",
                response.getStatusLine().getStatusCode(),
                response.getStatusLine().getReasonPhrase()
            )
        );
        result = new ResultImp(response, decode);
    }

    public Integer getStatus() {
        return result.getStatus();
    }

    public void setStatus(Integer status) {
        result.setStatus(status);
    }

    public HttpResponse getResponse() {
        return result.getResponse();
    }

    public void setResponse(HttpResponse response) {
        result.setResponse(response);
    }

    /**
     * get response body
     * @return response body
     */
    public String getBody() {
        return result.getBody();
    }

    /**
     * get http header
     * @return http headers
     */
    public Header[] getHeaders() {
        return result.getHeaders();
    }
}
