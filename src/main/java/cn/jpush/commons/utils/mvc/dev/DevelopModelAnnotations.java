
package cn.jpush.commons.utils.mvc.dev;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * 
 * @author yuanzp@jpush.cn
 * @date 2015-8-5 
 * @desc  方便测试 Rest Controller 
 */
public interface DevelopModelAnnotations {
	
	@Target( { ElementType.METHOD, ElementType.TYPE })
	@Retention(RetentionPolicy.RUNTIME)
	@interface  Debug {
		/**
		 *  to set the method deable model to return debug value ;
		 * @return
		 */
		boolean enable() default true;
		String result();
		DebugResultType resultType() default DebugResultType.JSON;
		int httpCode() default 200;
	}
	
}
