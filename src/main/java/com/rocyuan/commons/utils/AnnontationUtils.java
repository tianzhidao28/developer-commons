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

/**
 * Created by rocyuan on 2015/11/6.
 */


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * 注解工具类
 * @author yuanzp@jpush.cn
 * 2015-4-14
 */
public class AnnontationUtils {

    public static <T extends Annotation> T getFromMethed(Class<T> annotationClazz, Object o,
                                                         String methodName) {
        if (o == null) {
            return null;
        }
        Class<? extends Object> objectClass = o.getClass();
        try {
            Method method = objectClass.getDeclaredMethod(methodName);
            return method.getAnnotation(annotationClazz);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Annotation> T getFromMethedOrType(Class<T> annotationClazz, Object o,
                                                               String methodName) {
        if (o == null) {
            return null;
        }
        Class<? extends Object> objectClass = o.getClass();
        T annotation;
        try {
            Method method = objectClass.getDeclaredMethod(methodName);
            annotation = method.getAnnotation(annotationClazz);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        if (annotation != null) {
            return annotation;
        }
        return objectClass.getAnnotation(annotationClazz);
    }


    public static <T extends Annotation> T getFromMethed(Class<T> annotationClazz, Method method) {
        return method.getAnnotation(annotationClazz);
    }



}
