package com.yodean.job.core.service.resolver;

import org.quartz.ScheduleBuilder;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 6/22/18.
 */
public interface JobExpressResolver {

    ScheduleBuilder getScheduleBuilder();

    Date getStartDate() throws ParseException;

    Date getEndDate() throws ParseException;

    String getDescription() throws ParseException;

    List<Date> getNextFireTime(int numTimes) throws ParseException;
}
