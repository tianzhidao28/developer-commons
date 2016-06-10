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

import org.apache.commons.lang3.StringUtils;

/**
 * Created by rocyuan on 16/2/24.
 */
public class ParameterValidators {

    public static boolean notNull(Object ... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean notBlank(Object ... objs) {
        for (Object obj : objs) {
            if (obj == null || StringUtils.isBlank(obj.toString())) {
                return false;
            }
        }
        return true;
    }

    public static void requireNotNull(Object ... objs) {
        for (Object obj : objs) {
            if (obj == null) {
                throw new RuntimeException("有参数为空");
            }
        }
    }

}
