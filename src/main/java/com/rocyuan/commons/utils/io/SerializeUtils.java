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
package com.rocyuan.commons.utils.io;

/**
 * Created by rocyuan on 2015/10/13.
 */

import java.io.*;

public class SerializeUtils {

    public static final byte[] EMPTY_ARRAY = new byte[0];

    private SerializeUtils() {

    }

    public static byte[] serialize(Object obj) {
        if (obj != null) {
            ByteArrayOutputStream baos = null;
            ObjectOutputStream oos = null;
            try {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                oos.flush();
                baos.flush();
                byte[] b = baos.toByteArray();
                oos.close();
                oos = null;
                baos.close();
                baos = null;
                return b;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (oos != null) oos.close();
                    if (baos != null) baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static Object deserialize(byte[] b) {
        if (b != null) {
            ByteArrayInputStream bais = null;
            ObjectInputStream ois = null;
            try {
                bais = new ByteArrayInputStream(b);
                ois = new ObjectInputStream(bais);
                Object obj = ois.readObject();
                ois.close();
                ois = null;
                bais.close();
                bais = null;
                return obj;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (ois != null) ois.close();
                    if (bais != null) bais.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
