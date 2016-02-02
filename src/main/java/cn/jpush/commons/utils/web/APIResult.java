package cn.jpush.commons.utils.web;

import cn.jpush.commons.utils.JSONUtils;
import com.google.common.collect.ImmutableListMultimap;
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
