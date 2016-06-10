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
package com.rocyuan.commons.utils.cache;

import org.junit.Test;

/**
* CouchBaseUtils Tester.
*
* @author ${USER}
* @since <pre>三月 11, 2016</pre>
* @version 1.0
*/
public class CouchBaseUtilsTest {
    @Test
    public void simpleTest(){
        CouchBaseUtils.setData("app_cb_cache_all", "key", "value");
        System.out.println(CouchBaseUtils.getData("app_cb_cache_all", "key"));
        CouchBaseUtils.delData("app_cb_cache_all", "key");
    }
}
