package com.yodean.job.core.service.resolver;

import com.yodean.job.core.dto.JobExpress;
import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.ScheduleBuilder;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.quartz.spi.OperableTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by rick on 6/22/18.
 * 按天执行
 */
public class WeeklyExpressResolver extends AbstractExpressResolver {
    private static final Logger logger = LoggerFactory.getLogger(WeeklyExpressResolver.class);

    private Date startDate;

    public WeeklyExpressResolver(JobExpress jobExpress) {
        super(jobExpress);
    }

    /**
     * 获取合适的ScheduleBuilder
     * @return
     */
    @Override
    public ScheduleBuilder getScheduleBuilder() {

        ScheduleBuilder scheduleBuilder = CalendarIntervalScheduleBuilder.calendarIntervalSchedule()
                                            .withIntervalInWeeks(jobExpress.getInterval());

        return scheduleBuilder;
    }

    /**
     * 开始执行时间
     * @return
     * @throws ParseException
     */
    @Override
    public Date getStartDate() throws ParseException {
        if (startDate != null)
            return startDate;

        String startDateString = jobExpress.getStartDate() + " " + jobExpress.getStartTime() + ":00";

        startDate =  FORMAT_FULL.parse(startDateString);

        return startDate;
    }

    /**
     * 执行N次后结束
     * @return
     */
    @Override
    protected Date getEndDateIfAfter(int repeatCount) throws ParseException {
        return TriggerUtils.computeEndTimeToAllowParticularNumberOfFirings(getOperableTrigger(), null, repeatCount);
    }

    @Override
    public String getDescription() throws ParseException {
        StringBuilder sb = new StringBuilder();
        String interval = jobExpress.getInterval() > 1 ? jobExpress.getInterval()  + "" : "";

        sb.append("每").append(interval).append("天发生，生效时间：").append(jobExpress.getStartDate());

        if (Objects.nonNull(getEndDate())) {
            sb.append("至").append(FORMAT_DATE.format(getEndDate()));
        }

        sb.append("，从").append(FORMAT_TIME.format(getStartDate())).append("到").append("...");

        if (Objects.nonNull(repeatCount)) {
            sb.append("，共有").append(repeatCount).append("个事件");
        }

        logger.info("execute description: {}", sb.toString());
        return sb.toString();
    }

    @Override
    public List<Date> getNextFireTime(int numTimes) throws ParseException {
        List<Date> list;

        if (repeatCount != null)
            list = TriggerUtils.computeFireTimes(getOperableTrigger(), null, repeatCount);
        else
            list = TriggerUtils.computeFireTimesBetween(getOperableTrigger(), null, getStartDate(), getEndDate());


        for (Date date : list) {
            logger.info("fire at: {}", FORMAT_FULL.format(date));
        }

        return list;
    }

    private OperableTrigger getOperableTrigger() throws ParseException {
        CalendarIntervalTriggerImpl calendarIntervalTrigger = new CalendarIntervalTriggerImpl();
        calendarIntervalTrigger.setStartTime(getStartDate());
        calendarIntervalTrigger.setRepeatInterval(jobExpress.getInterval());
        return calendarIntervalTrigger;
    }

}