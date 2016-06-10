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
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;



/**
 * Created by rocyuan on 2015/11/11.
 */
public class QuartzTest {



    public void example() throws SchedulerException {


        SchedulerFactory schFactory = new StdSchedulerFactory();
        Scheduler sch = schFactory.getScheduler();
        sch.start();

        ScheduleJob resendJob = new ScheduleJob("resendJob","sms","cn.jpush.sms.report.jobs.SmsResendJob","execute","1 0/20 * * * ?","找出需要重发的短信 重发",null,true);
        ScheduleUtils.createScheduleJob(sch, resendJob);

    }

}
