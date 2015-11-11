package cn.jpush.commons.utils.quartz.job;


import cn.jpush.commons.utils.quartz.model.ScheduleJob;
import cn.jpush.commons.utils.quartz.util.ScheduleUtils;
import cn.jpush.commons.utils.quartz.util.TaskUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class SyncJobFactory implements Job {

	public final static Logger log = LoggerFactory.getLogger(SyncJobFactory.class);
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
	
	     	ScheduleJob scheduleJob = (ScheduleJob) context.getMergedJobDataMap().get(ScheduleUtils.JOB_PARAM_KEY);
	       log.info(String.format( " job execute : time %s :  %s " , new Date() ,scheduleJob )) ;
	       TaskUtil.invokeMethod(scheduleJob);
	}

}
