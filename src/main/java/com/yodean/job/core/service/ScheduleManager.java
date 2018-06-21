package com.yodean.job.core.service;

import com.yodean.job.core.dto.CronExpress;
import org.quartz.SchedulerException;

/**
 * Created by rick on 6/20/18.
 */
public interface ScheduleManager {

    void addSchedule(CronExpress cronExpress) throws SchedulerException;

    boolean deleteSchedule(CronExpress cronExpress) throws SchedulerException;

    void updateSchedule(CronExpress cronExpress) throws SchedulerException;
}
