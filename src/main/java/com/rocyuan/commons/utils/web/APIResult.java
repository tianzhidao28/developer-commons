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
package com.rocyuan.commons.utils.web;

import com.rocyuan.commons.utils.JSONUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.htrace.fasterxml.jackson.annotation.JsonInclude;

/**
 * 可以作为统一的API返回对象
 * @author admin@rocyuan.com
 * @date 2015-7-29
 * @desc
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResult<T> {
    public Integer code ;
    public String message ;
    public T data ;

    private APIResult() {

    }
    public static Builder newBuilder(APICode apiCode) {
        return new Builder(apiCode);
    }
    @SuppressWarnings("unchecked")
    public static <T>Builder newBuilder(T data) {
        return new Builder(data);
    }

    public static class Builder<T> {
        Integer code ;
        String message ;
        T data ;
        private Builder(APICode apiCode){
            this.code = apiCode.getCode();
            this.message = apiCode.getMessage();
        }
        private Builder(T data ) {
            this.data = data ;
        }
        public Builder setData(T data) {
            this.data = data ;
            return this ;
        }
        public Builder setAPICode(APICode apiCode) {
            this.code = apiCode.getCode();
            this.message = apiCode.getMessage();
            return this ;
        }

        public Builder setMessage(String msg){
            this.message=msg;
            return this;
        }


        public APIResult build() {
            APIResult apiResult = new APIResult();
            if (code == null) {
                code = 0 ;
            }
            if (message == null) {
                message="ok";
            }
            apiResult.code = this.code ;
            apiResult.message = this.message ;
            apiResult.data = this.data;
            return apiResult ;
        }

    }


    public  String toErrJson() {
        ImmutableMap map = ImmutableMap.of("code",code,"message",message);
        return JSONUtils.toString(map);
    }




}
