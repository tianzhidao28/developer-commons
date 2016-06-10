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

package com.rocyuan.commons.utils.mvc;


import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-6-24
 * @desc  Spring3.1 之后的新版本 ; 处理自定义绑定注解  @RequestAttribute 从request attribute 里 获取注解
 * @resolver:  配置无效的情况解决  http://blog.csdn.net/truong/article/details/30972095    <mvc:annotation-driven />  放下面
 */
public class RequestAttributeMethodArgumentResolver implements
		HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(RequestAttribute.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter,
			ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory) throws Exception {
		RequestAttribute attrAnno = parameter.getParameterAnnotation(RequestAttribute.class);
		String attrName = attrAnno.value() ;
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Object value = request.getAttribute(attrName);
		if ( value == null ) {
			return WebArgumentResolver.UNRESOLVED;
		}
		return value;
	}

}
