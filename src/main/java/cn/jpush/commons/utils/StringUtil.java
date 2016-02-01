package cn.jpush.commons.utils;

import java.util.Random;

/**
 * 字符串操作 工具类
 */
public class StringUtil {
    private static final String base = "abcdefghijklmnopqrstuvwxyzQWERTYUIOPLKJHGFDSAZXCVBNM0123456789";

    private StringUtil() {}
    
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(base.charAt(random.nextInt(base.length())));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String str) {
        return !(str != null && str.length() > 0);
    }
    
    public static boolean isNotEmpty(String str) {
        return str != null && str.length() > 0;
    }

}
