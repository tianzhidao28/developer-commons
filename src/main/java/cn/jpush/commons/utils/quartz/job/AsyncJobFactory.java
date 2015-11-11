package cn.jpush.commons.utils.quartz.job;


import cn.jpush.commons.utils.quartz.model.ScheduleJob;
import cn.jpush.commons.utils.quartz.util.ScheduleUtils;
import cn.jpush.commons.utils.quartz.util.TaskUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * 
 * @author yuanzp@jpush.cn
 * 2015-4-13
 */
@DisallowConcurrentExecution
public class AsyncJobFactory implements Job {

	public final static Logger log = LoggerFactory.getLogger(AsyncJobFactory.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	        ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleUtils.JOB_PARAM_KEY);
	       log.info(String.format( " job execute : time %s :  %s " , new Date() ,scheduleJob )) ;
	       TaskUtil.invokeMethod(scheduleJob);
	}

}
