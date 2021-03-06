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
package com.rocyuan.commons.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 配置在 xml bean里使用
 * Created by rocyuan on 2015/11/6.
 */


//@Service
public final class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static Object getBean(String beanName){
        return context.getBean(beanName);
    }

    public static <T> T  getBean(String beanName ,Class<T> clazz){
        return context.getBean(beanName, clazz);
    }

    public static<T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    @Override
    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        SpringContextUtil.context = context;

    }
}
