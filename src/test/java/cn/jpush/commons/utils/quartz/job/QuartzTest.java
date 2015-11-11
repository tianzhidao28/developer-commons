package cn.jpush.commons.utils.quartz.job;

import cn.jpush.commons.utils.quartz.model.ScheduleJob;
import cn.jpush.commons.utils.quartz.util.ScheduleUtils;
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