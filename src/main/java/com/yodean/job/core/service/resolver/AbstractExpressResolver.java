package com.yodean.job.core.service.resolver;

import com.yodean.job.core.dto.JobExpress;
import org.quartz.TimeOfDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * Created by rick on 6/22/18.
 */
public abstract class AbstractExpressResolver implements JobExpressResolver {

    protected static final String SEPARATOR_OF_COLON = ":";

    protected static final String END_DATE_BY = "by";

    protected static final String END_DATE_AFTER = "after";

    protected static final SimpleDateFormat FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    protected static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    protected static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm");

    protected JobExpress jobExpress;

    protected Integer repeatCount;
    /**
     * 截止日期
     */
    protected Date endDate;

    public AbstractExpressResolver(JobExpress jobExpress) {
        this.jobExpress = jobExpress;
    }


    protected abstract Date getEndDateIfAfter(int repeatCount) throws ParseException;

    @Override
    public Date getEndDate() throws ParseException {
        if (endDate != null)
            return endDate;

        String endDateString = jobExpress.getEndDate();

        if (Objects.nonNull(endDateString)) {
            String[] params = endDateString.split(SEPARATOR_OF_COLON);

            if (Objects.equals(END_DATE_BY, params[0])) {
//                endDate = FORMAT_DATE.parse(params[1]);
                endDate = FORMAT_FULL.parse(params[1] + " 23:59:59"); //包含当天
            } else if (Objects.equals(END_DATE_AFTER, params[0])) {
                //循环N次停止
                this.repeatCount = Integer.parseInt(params[1]);
                endDate = getEndDateIfAfter(repeatCount);
            }
        }

        return endDate;
    }

    protected TimeOfDay getStartTimeOfDay() throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(getStartDate());

        TimeOfDay timeOfDay = new TimeOfDay(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        return timeOfDay;
    }

}
