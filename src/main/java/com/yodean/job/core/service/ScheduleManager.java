package com.yodean.job.core.service;

import com.yodean.job.core.dto.JobExpress;
import org.quartz.SchedulerException;

import java.text.ParseException;

/**
 * Created by rick on 6/20/18.
 */
public interface ScheduleManager {

    void addSchedule(JobExpress cronExpress) throws SchedulerException, ParseException;

    boolean deleteSchedule(JobExpress cronExpress) throws SchedulerException;

    void updateSchedule(JobExpress cronExpress) throws SchedulerException;
}
