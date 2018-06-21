package com.yodean.job;

import com.yodean.job.core.dto.CronExpress;
import com.yodean.job.core.service.ScheduleManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobCoreApplicationTests {

	@Autowired
	private ScheduleManager scheduleManager;

	@Test
	public void testDaily() throws ParseException, SchedulerException {
		CronExpress cronExpress = new CronExpress(CronExpress.CategoryEnum.TASK, "123456");
		cronExpress.setRepeatsEnum(CronExpress.RepeatsEnum.DAILY);
		cronExpress.setInterval(2);

		cronExpress.setStartDate("2018-06-22");
		cronExpress.setStartTime("09:00");

		cronExpress.setEndTime("18:00");

		//时间结束
//        cronExpress.setEndDate("by:2018-10-30");
		//执行3次后结束
        cronExpress.setEndDate("after:3");

		cronExpress.getDescription();
		cronExpress.getNextFireTime();

//		scheduleManager.addSchedule(cronExpress);
	}

	@Test
	public void testWeekly() throws ParseException, SchedulerException {
		CronExpress cronExpress = new CronExpress(CronExpress.CategoryEnum.TASK, "123456");
		cronExpress.setRepeatsEnum(CronExpress.RepeatsEnum.WEEKLY);
		cronExpress.setInterval(3); //每隔3周不起效果
        cronExpress.setSpecify("1,2,7");

		cronExpress.setStartDate("2018-06-22");

		cronExpress.setStartTime("09:00");
		cronExpress.setEndTime("18:00");

		//时间结束
        cronExpress.setEndDate("by:2018-10-30");
//
		//执行3次后结束
//        cronExpress.setEndDate("after:3");
//
		cronExpress.getDescription();
		cronExpress.getNextFireTime();

//		scheduleManager.addSchedule(cronExpress);
	}



	@Test
	public void testMonthly() throws ParseException, SchedulerException {
		CronExpress cronExpress = new CronExpress(CronExpress.CategoryEnum.TASK, "123456");
        cronExpress.setRepeatsEnum(CronExpress.RepeatsEnum.MONTHLY);
        cronExpress.setInterval(2);

        cronExpress.setSpecify("week:1L"); //最后一个周末
//		cronExpress.setSpecify("week:2,3,4"); //周一周二周三
//		cronExpress.setSpecify("week:1#3"); //第三周周末
//		cronExpress.setSpecify("day:1,2,3");  //1 和 2号 3号

        cronExpress.setStartDate("2018-06-22");

        cronExpress.setStartTime("09:00");
		cronExpress.setEndTime("18:00");

        //时间结束
//        cronExpress.setEndDate("by:2018-10-30");


		//执行3次后结束
//        cronExpress.setEndDate("after:3");

		cronExpress.getDescription();
        cronExpress.getNextFireTime();

//		scheduleManager.addSchedule(cronExpress);
	}

	@Test
	public void testYearly() throws ParseException, SchedulerException {
		CronExpress cronExpress = new CronExpress(CronExpress.CategoryEnum.TASK, "123456");
		cronExpress.setRepeatsEnum(CronExpress.RepeatsEnum.YEARLY);
		cronExpress.setInterval(2);

		cronExpress.setSpecify("1,2"); //1月 2月

		cronExpress.setStartDate("2018-06-22");

		cronExpress.setStartTime("09:00");
		cronExpress.setEndTime("18:00");

		//时间结束
//        cronExpress.setEndDate("by:2018-10-30");


		//执行3次后结束
//        cronExpress.setEndDate("after:3");

		cronExpress.getDescription();
		cronExpress.getNextFireTime();

//		scheduleManager.addSchedule(cronExpress);
	}



	@Test
	public void testDelete() throws ParseException, SchedulerException {
		CronExpress cronExpress = new CronExpress(CronExpress.CategoryEnum.TASK, "123456");

		scheduleManager.deleteSchedule(cronExpress);
	}


}
