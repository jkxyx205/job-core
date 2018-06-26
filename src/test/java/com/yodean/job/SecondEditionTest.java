package com.yodean.job;

import com.yodean.job.core.dto.JobExpress;
import com.yodean.job.core.service.resolver.DailyExpressResolver;
import com.yodean.job.core.service.resolver.JobExpressResolver;
import com.yodean.job.core.service.resolver.WeeklyExpressResolver;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.DateBuilder;
import org.quartz.TimeOfDay;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rick on 6/22/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SecondEditionTest {

    @Test
    public void testDaily() throws ParseException {

        JobExpress jobExpress = new JobExpress(JobExpress.CategoryEnum.TASK, "123456");
        jobExpress.setRepeatsEnum(JobExpress.RepeatsEnum.DAILY);
        jobExpress.setInterval(2);

        jobExpress.setStartDate("2018-06-24");
        jobExpress.setStartTime("09:00");

//        jobExpress.setEndTime("18:00");

        //时间结束
        jobExpress.setEndDate("by:2018-07-10"); //包含10号
        //执行3次后结束
//        jobExpress.setEndDate("after:10");

        JobExpressResolver jobExpressResolver = new DailyExpressResolver(jobExpress);

        jobExpressResolver.getDescription();
        jobExpressResolver.getNextFireTime(20);

//		scheduleManager.addSchedule(cronExpress);

    }

    @Test
    public void testWeekly() throws ParseException {

        JobExpress jobExpress = new JobExpress(JobExpress.CategoryEnum.TASK, "123456");
        jobExpress.setRepeatsEnum(JobExpress.RepeatsEnum.WEEKLY);
        jobExpress.setInterval(2);

        jobExpress.setStartDate("2018-06-24");
        jobExpress.setStartTime("09:00");

//        jobExpress.setEndTime("18:00");

        //时间结束
//        jobExpress.setEndDate("by:2018-07-10"); //包含10号
        //执行3次后结束
//        jobExpress.setEndDate("after:10");

        JobExpressResolver jobExpressResolver = new WeeklyExpressResolver(jobExpress);

        jobExpressResolver.getDescription();
        jobExpressResolver.getNextFireTime(20);

//		scheduleManager.addSchedule(cronExpress);

    }

    @Test
    public void testTrigger() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //CalendarIntervalTriggerImpl
        CalendarIntervalTriggerImpl calendarIntervalTrigger = new CalendarIntervalTriggerImpl();

        calendarIntervalTrigger.setStartTime(new Date());
        calendarIntervalTrigger.setRepeatIntervalUnit(DateBuilder.IntervalUnit.WEEK);
        calendarIntervalTrigger.setRepeatInterval(2);



        //DailyTimeIntervalTriggerImpl
        DailyTimeIntervalTriggerImpl dailyTimeIntervalTrigger = new DailyTimeIntervalTriggerImpl();
        Date now = new Date();

        dailyTimeIntervalTrigger.setStartTime(now);
        dailyTimeIntervalTrigger.setStartTimeOfDay(new TimeOfDay(now.getHours(), now.getMinutes()));
        dailyTimeIntervalTrigger.setRepeatIntervalUnit(DateBuilder.IntervalUnit.HOUR);

        dailyTimeIntervalTrigger.setRepeatInterval(24);
        Set<Integer> daysOfWeek = new HashSet<>();
        daysOfWeek.add(1);
        daysOfWeek.add(7);
        dailyTimeIntervalTrigger.setDaysOfWeek(daysOfWeek);

        List<Date> list = TriggerUtils.computeFireTimes(calendarIntervalTrigger, null, 20);
        for (Date date: list) {
            System.out.println(sdf.format(date));
        }
    }


}
