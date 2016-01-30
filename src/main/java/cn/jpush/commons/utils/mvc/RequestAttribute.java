/*******************************************************************************
 * Copyright (c) 2015 www.jpay.jpush.cn
 *
 *******************************************************************************/
package cn.jpush.commons.utils.mvc;

import java.lang.annotation.*;

/**
 *  
 * @author yuanzp@jpush.cn
 * @date 2015-7-10 
 * @desc  MVC 注解获取 request  attribute 里的数据
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestAttribute {
	String value();
}
