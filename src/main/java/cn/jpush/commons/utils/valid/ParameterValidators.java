package cn.jpush.commons.utils.valid;

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
