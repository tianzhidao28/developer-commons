package cn.jpush.commons.utils.quartz.util;


import cn.jpush.commons.utils.AnnontationUtils;
import cn.jpush.commons.utils.SpringContextUtil;
import cn.jpush.commons.utils.quartz.annotation.JobExecute;
import cn.jpush.commons.utils.quartz.model.ScheduleJob;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.support.TaskUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 通过反射 动态执行方法
 * @author yuanzp@jpush.cn
 * 2015-4-14
 */
public class TaskUtil {

	
	public final static Logger log = LoggerFactory.getLogger(TaskUtils.class);
	
	/**
	 *  动态运行job 
	 * @desc:  ReflectionUtils.invokeMethod  Spring 内部有这个方法
	 * @param job
	 */
	public static void invokeMethod ( ScheduleJob job ) {
		Object  obj = null ;
		Class objClass = null;
		Method  method = null ;
		String springId = job.getSpringBeanId() ;
		String param = job.getParam();
		if ( StringUtils.isNotBlank(springId) ) {
			obj = SpringContextUtil.getBean(springId);
		} else if ( StringUtils.isNotBlank(job.getClassName() )  ){
			try {
				objClass = Class.forName(job.getClassName());
				obj = objClass.newInstance();
			} catch (Exception e) {				
				e.printStackTrace();
			}			
		}
		obj = Preconditions.checkNotNull(obj);
		objClass = obj.getClass();
		if ( StringUtils.isNotBlank(job.getMethod()) ) {
			try {
				method = objClass.getDeclaredMethod( job.getMethod()) ;
			} catch (NoSuchMethodException e) {			
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		} else {		
			/**
			 *  如果 有注解标示的 执行方法 则 就是这个方法  否则 随便执行最后一个
			 */
			Method[] methods = objClass.getDeclaredMethods() ;		
			for ( Method m : methods ) {
				JobExecute jobExecute = AnnontationUtils.getFromMethed(JobExecute.class, m)	 ;
				method = m ;
				if ( jobExecute !=null ) {
					// 找到了需要去执行的方法了
					break ;
				}
								
			}
		}		
		method = Preconditions.checkNotNull(method);	
		try {
			if ( StringUtils.isNotBlank(param) ) {
				String [] paramsArry = param.split(",");
				int type = NumberUtils.toInt(paramsArry[0], -1);
				String orderNo = StringUtils.trim(paramsArry[1]);
				Object[] params = new Object[paramsArry.length] ;
				method.invoke(obj , type , orderNo);
			} else {
				method.invoke(obj);
			}
			
			log.info(String.format("正在执行 Job( %s ): %s.%s  ", job.getDescription() ,objClass.getName() ,method.getName()  ));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
