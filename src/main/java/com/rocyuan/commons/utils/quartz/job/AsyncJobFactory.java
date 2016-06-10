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
package com.rocyuan.commons.utils.quartz.job;


import com.rocyuan.commons.utils.quartz.model.ScheduleJob;
import com.rocyuan.commons.utils.quartz.util.ScheduleUtils;
import com.rocyuan.commons.utils.quartz.util.TaskUtil;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 *
 * @author admin@rocyuan.com
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
