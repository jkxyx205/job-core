package com.yodean.job.core.service;


import com.yodean.job.core.dto.ScheduleDetail;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
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

    @Autowired
    @Qualifier("schedulerFactoryBean")
    private  Scheduler scheduler;

    /***
     * 添加任务
     * @param schedule
     * @throws SchedulerException
     */
    public void addJob(ScheduleDetail schedule) throws SchedulerException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");

        Date now = new Date();
        logger.info("current date: {}", sdf.format(now));

        now.setTime(now.getTime() + 5000L);

        Date start = now;
        logger.info("start date: {}", sdf.format(start));

        JobDetail job =
                newJob(schedule.getJob())
                        .withIdentity(schedule.getName(), schedule.getGroupName())
                        .requestRecovery(true)
                        .usingJobData(new JobDataMap(schedule.getJobProps()))
//                        .storeDurably() 任务结束后不保留
                        .build();

        ScheduleBuilder<CronTrigger> scheduleBuilder = CronScheduleBuilder.cronSchedule(schedule.getCronExpression());

        Date endDate = TriggerUtils.computeEndTimeToAllowParticularNumberOfFirings((OperableTrigger)((CronScheduleBuilder)scheduleBuilder).build(),null, 5);
        logger.info("start endDate: {}", sdf.format(endDate));

        //每隔5分钟执行1次，执行10次
//        SimpleScheduleBuilder.repeatMinutelyForTotalCount(10, 5);

         //周一到周五执行
         //DailyTimeIntervalScheduleBuilder.dailyTimeIntervalSchedule().onMondayThroughFriday();


        Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(job)
                .startAt(start)
                .endAt(endDate)
//                .usingJobData()
//              .startAt() //首次触发时间 默认当前时间
//              .endAt() //截止时间，就算指定的执行次数没有执行完也立即结束
                .withIdentity(schedule.getTriggerName(), schedule.getTriggerGroupName())
                .withSchedule(scheduleBuilder).build();

//        ((MutableTrigger)trigger).setMisfireInstruction(CronTrigger.MISFIRE_INSTRUCTION_DO_NOTHING);
//        trigger.getNextFireTime()
//        trigger.getFinalFireTime()
        logger.info(((CronTrigger)trigger).getExpressionSummary());
        //返回最近一次执行的时间
        Date nextFireTime = scheduler.scheduleJob(job, trigger);
        logger.info("Next Fire Time {}", sdf.format(nextFireTime));

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
