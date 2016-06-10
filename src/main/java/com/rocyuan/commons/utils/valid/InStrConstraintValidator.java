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
package com.rocyuan.commons.utils.valid;


import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *
 * @author yuanzp@jpush.cn
 * @date 2015-6-24
 * @desc   自定义 验证器的实现
 *    <bean id="validator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean" />
 *    <mvc:annotation-driven  conversion-service="conversionService"  validator="validator"/>
 */
public class InStrConstraintValidator implements ConstraintValidator<InStr, String> {

    String [] valuesIn ;
    @Override
    public void initialize(InStr constraintAnnotation) {
        valuesIn = constraintAnnotation.value() ;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if ( valuesIn !=null ) {
            for ( String v : valuesIn ) {
                if ( v.equals(value) ) {
                    return true ;
                }
            }
        }
        return false;
    }

}
