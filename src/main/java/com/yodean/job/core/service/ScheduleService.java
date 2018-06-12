package com.yodean.job.core.service;


import com.yodean.job.core.bo.ScheduleDetail;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.quartz.JobBuilder.newJob;

/**
 * Created by rick on 2018/3/23.
 */
@Service
public class ScheduleService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    private final Scheduler scheduler;

    @Autowired
    public ScheduleService(SchedulerFactoryBean schedulerFactory) {
        this.scheduler = schedulerFactory.getScheduler();
    }

    /***
     * 添加任务
     * @param schedule
     * @throws SchedulerException
     */
    public void addJob(ScheduleDetail schedule) throws SchedulerException {
        JobDetail job =
                newJob(schedule.getJob())
                        .withIdentity(schedule.getName(), schedule.getGroupName())
                        .requestRecovery(true)
                        .usingJobData(new JobDataMap(schedule.getJobProps()))
                        .storeDurably()
                        .build();

        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .withIdentity(schedule.getTriggerName(), schedule.getTriggerGroupName())
                .withSchedule(scheduleBuilder).build();

        scheduler.scheduleJob(job, trigger);


    }


    /***
     * 修改任务
     * @param schedule
     * @throws SchedulerException
     */
    public void updateJob(ScheduleDetail schedule) throws SchedulerException {
        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(schedule.getTriggerName(), schedule.getTriggerGroupName())
                .withSchedule(scheduleBuilder).build();

        scheduler.rescheduleJob(new TriggerKey(schedule.getTriggerName(), schedule.getTriggerGroupName()), trigger);
    }


    /***
     * 删除任务
     * @param name
     * String groupName
     * @return
     * @throws SchedulerException
     */
    public boolean deleteJob(String name, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(name, groupName);
        return scheduler.deleteJob(jobKey);
    }

    /***
     * 暂停任务
     * @param name
     * String groupName
     * @return
     * @throws SchedulerException
     */
    public void pauseJob(String name, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(name, groupName);
        scheduler.pauseJob(jobKey);
    }

    /***
     * 恢复任务
     * @param name
     * String groupName
     * @return
     * @throws SchedulerException
     */
    public void resumeJob(String name, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(name, groupName);
        scheduler.resumeJob(jobKey);
    }

    /**
     * 立即执行一次
     * @param name
     * @param groupName
     * @throws SchedulerException
     */
    public void executeOnceJob(String name, String groupName) throws SchedulerException {
        JobKey jobKey = new JobKey(name, groupName);
        scheduler.triggerJob(jobKey);
    }

    public List<JobKey> listAllJobs() throws SchedulerException {

        List<JobKey> jobKeyList = new ArrayList<>(scheduler.getJobGroupNames().size());

        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();

                //get job's trigger
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                Date nextFireTime = triggers.get(0).getNextFireTime();

                logger.info("[jobName] : " + jobName + " [groupName] : "
                        + jobGroup + " - " + nextFireTime);

                jobKeyList.add(jobKey);

            }

        }

        return jobKeyList;
    }
}
