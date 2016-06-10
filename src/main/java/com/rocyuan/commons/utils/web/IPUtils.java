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
package com.rocyuan.commons.utils.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by rocyuan on 16/1/31.
 */
public class IPUtils {

    // ip格式 0~255.0~255.0~255.1~255
    private static final String REGEX_IP = "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|\\d)\\.){3}(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]\\d|[1-9])";
    // ip区间 (格式为 ip-ip，例如：211.151.74.1-211.151.74.100)
    private static final String REGEX_INTERVAL_IP = REGEX_IP + "-" + REGEX_IP;

    /**
     * 获得用户IP
     *
     * @param request
     * @return
     */
    public static String getRemoteAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip == null || ip.length() == 0 || ip.equalsIgnoreCase("unknown"))
            ip = request.getRemoteAddr();

        return ip;
    }

    /**
     * 字符型IP转换成long型
     *
     * @param ip
     * @return
     */
    public static Long ipToLong(String ip) {
        long result = 0;
        if (isIpFormat(ip)) {
            String section[] = ip.split("\\.");
            if (section.length == 4) {
                result += Long.parseLong(section[0]) << 24;
                result += Long.parseLong(section[1]) << 16;
                result += Long.parseLong(section[2]) << 8;
                result += Long.parseLong(section[3]);
            }
        }
        return result;
    }

    /**
     * 将long型IP转换成字符型
     *
     * @param value
     * @return
     */
    public static String longToIp(long value) {
        if (value < 1)
            throw new IllegalArgumentException(value + "is not a IP long value.");

        StringBuilder ip = new StringBuilder();
        ip.append(String.valueOf((value >>> 24))).append("."); // 直接右移24位
        ip.append(String.valueOf((value & 0x00FFFFFF) >>> 16)).append("."); // 将高8位置0，然后右移16位
        ip.append(String.valueOf((value & 0x0000FFFF) >>> 8)).append("."); // 将高16位置0，然后右移8位
        ip.append(String.valueOf((value & 0x000000FF))); // 将高24位置0
        return ip.toString();
    }

    /**
     * 根据传入的ip区间，
     *
     * @param intervalIp
     * @return 返回的数组有两个元素，第一个是较小的，第二的是较大的
     */
    public static long[] getIntervalIpArr(String intervalIp) {
        if (!isIntervalIpFormat(intervalIp))
            throw new IllegalArgumentException("interval ip format error !");

        String[] ips = intervalIp.split("-");
        long ip1 = ipToLong(ips[0]);
        long ip2 = ipToLong(ips[1]);

        return new long[] { Math.min(ip1, ip2), Math.max(ip1, ip2) };
    }

    public static boolean isIpFormat(String ip) {
        return ip != null && ip.length() > 0 && ip.matches(REGEX_IP);
    }

    public static boolean isIpArrFormat(String[] ips) {
        if (ips == null || ips.length < 1)
            return false;

        for (String ip : ips) {
            if (!isIpFormat(ip))
                return false;
        }

        return true;
    }

    public static boolean isIntervalIpFormat(String intervalIp) {
        return intervalIp != null && intervalIp.length() > 0 && intervalIp.matches(REGEX_INTERVAL_IP);
    }
}
