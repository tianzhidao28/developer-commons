package cn.jpush.commons.utils;

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
