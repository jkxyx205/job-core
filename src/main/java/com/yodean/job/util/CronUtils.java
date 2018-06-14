package com.yodean.job.util;

import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rick on 6/11/18.
 */
public final class CronUtils {

    private CronUtils() {}

    /**
     *
     * @param cron
     * @param start
     * @param end
     * @return
     * @throws ParseException
     */
    public static List<Date> getExecuteTime(String cron, Date start, Date end) throws ParseException {
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(cron);


        List<Date> dates = TriggerUtils.computeFireTimesBetween(
                cronTriggerImpl, null
                , start
                , end);


        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        for (Date date : dates) {
            System.out.println(dateFormat.format(date));
        }

        return null;
    }



    public static void getNextExecuteTime(String cron, Date endDate) {

    }

    public static void main(String[] args) throws ParseException {

        CronUtils.getExecuteTime("0 0 0 1,2 8 ? 2019", new Date(), new Date());
//        CronUtils.getNextExecuteTime(1, 23, "0/30 * * * * ? ");
    }
}
