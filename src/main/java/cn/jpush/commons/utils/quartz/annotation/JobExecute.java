package cn.jpush.commons.utils.quartz.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注在Job上 哪些是要被执行的方法 ; 
 * 默认找有这个注解的方法 ； 没有就找 去除get 和set之外的方法
 * @author yuanzp@jpush.cn
 * 2015-4-14
 */
@Target( { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface JobExecute {
	
}
