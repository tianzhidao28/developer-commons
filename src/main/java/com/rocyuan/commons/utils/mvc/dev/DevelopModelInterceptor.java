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

package com.rocyuan.commons.utils.mvc.dev;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rocyuan.commons.utils.web.Servlets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;



/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-8-5
 * @blog blog.rocyuan.com
 * @desc  拦截Controller 判断是否是 Debug模式 决定返回值 ;
 * 				  已经被拦截了 你传什么都无所谓了,此时你可以按正常状态开发
 */
public class DevelopModelInterceptor extends HandlerInterceptorAdapter {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	// 可以在配置文件里设置其 开关
	private boolean debugable = false ;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		if (debugable) {
			if (handler instanceof HandlerMethod) {
				HandlerMethod handlerMethod = (HandlerMethod) handler;
				DevelopModelAnnotations.Debug debug = handlerMethod.getMethodAnnotation(DevelopModelAnnotations.Debug.class);
				if (debug!=null && debug.enable()) {
					String result = debug.result();
					String mediaType = debug.resultType().name();
					int httpCode = debug.httpCode();
					Servlets.writeResponse(response, result , mediaType , httpCode);
					return false;
				}
			}
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		super.afterConcurrentHandlingStarted(request, response, handler);
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
	}

	public boolean isDebugable() {
		return debugable;
	}

	public void setDebugable(boolean debugable) {
		this.debugable = debugable;
	}




}
