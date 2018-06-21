package com.yodean.job.core.service;

import com.yodean.job.core.dto.CronExpress;
import com.yodean.job.job.AlertJob;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by rick on 6/20/18.
 */
@Service
public class ScheduleManagerImpl implements ScheduleManager {

    public static final Logger logger = LoggerFactory.getLogger(ScheduleManagerImpl.class);

    @Autowired
    @Qualifier("schedulerFactoryBean")
    private Scheduler scheduler;


    /**
     * 添加任务
     * @param cronExpress
     * @throws SchedulerException
     */
    @Override
    public void addSchedule(CronExpress cronExpress) throws SchedulerException {
        JobDetail job =
                newJob(AlertJob.class)
                        .requestRecovery(true)
                        .withIdentity(cronExpress.getName())
                        .usingJobData(new JobDataMap(cronExpress.getJobDateMap()))
                        .build();

        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress.getCronString());


         TriggerBuilder tb = TriggerBuilder.newTrigger()
                .forJob(job)
                .withIdentity(cronExpress.getName())
                .startAt(cronExpress.getStartDate())
                .withSchedule(scheduleBuilder);

        if (Objects.nonNull(cronExpress.getEndDate())) {
            tb.endAt(cronExpress.getEndDate());
        }

        Trigger trigger = tb.build();

        scheduler.scheduleJob(job, trigger);
    }

    /***
     * 修改任务
     * @param cronExpress
     * @throws SchedulerException
     */
    @Override
    public void updateSchedule(CronExpress cronExpress) throws SchedulerException {
        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpress.getCronString());

        TriggerBuilder tb = TriggerBuilder.newTrigger()
                .withIdentity(cronExpress.getName())
                .startAt(cronExpress.getStartDate())
                .withSchedule(scheduleBuilder);

        if (Objects.nonNull(cronExpress.getEndDate())) {
            tb.endAt(cronExpress.getEndDate());
        }

        Trigger trigger = tb.build();

        scheduler.rescheduleJob(new TriggerKey(cronExpress.getName()), trigger);
    }

    /**
     * 删除任务
     * @param cronExpress
     * @return
     * @throws SchedulerException
     */
    @Override
    public boolean deleteSchedule(CronExpress cronExpress) throws SchedulerException {
        JobKey jobKey = new JobKey(cronExpress.getName());
        return scheduler.deleteJob(jobKey);
    }

}