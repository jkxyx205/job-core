package com.yodean.job.core.dto;


import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by rick on 6/20/18.
 */
public class CronExpress {

    private static final Logger logger = LoggerFactory.getLogger(CronExpress.class);

    private static final SimpleDateFormat FORMAT_FULL = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    private static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat("HH:mm");

    private static final int DEFAULT_NUM_TIMES = 20;

    private static final String DATE_SPLIT = "-";

    private static final String TIME_SPLIT = ":";

    private static final String SPECIFY_TYPE_DAY = "day";

    private static final String SPECIFY_TYPE_WEEK = "week";

    private static final String END_DATE_TYPE_BY = "by";

    private static final String END_DATE_TYPE_AFTER = "after";


    /**
     * 分类
     */
    private CategoryEnum categoryEnum;

    /**
     * 实例id
     */
    private String instance;

    /**
     * 重复类型
     */
    private RepeatsEnum repeatsEnum;

    /**
     * 间隔
     */
    private Integer interval;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束日期
     * after: 10
     * by:2015-10-19
     */
    private String endDate;

    /**
     * 结束时间
     * 23:00
     */
    private String endTime;

    /**
     * 指定范围
     */
    private String specify;

    private String cronString;

    private Date startDateTime;

    private Date endDateTime;

    private String description;

    public CronExpress(CategoryEnum categoryEnum, String instance) {
        this.instance = instance;
        this.categoryEnum = categoryEnum;
    }

    public static enum RepeatsEnum {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }

    public static enum CategoryEnum {
        TASK, MEETING, NOTE
    }

    public RepeatsEnum getRepeatsEnum() {
        return repeatsEnum;
    }

    public void setRepeatsEnum(RepeatsEnum repeatsEnum) {
        this.repeatsEnum = repeatsEnum;
    }

    public Integer getInterval() {
        return interval;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public Date getStartDate() {
        try {
            return FORMAT_DATE.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getEndDate() {

        if (Objects.isNull(endDate))
            return null;

        if (Objects.nonNull(endDateTime))
            return endDateTime;


        String[] params = endDate.split(TIME_SPLIT);

        try {
            if (Objects.equals(END_DATE_TYPE_AFTER, params[0])) { // 次数
                endDateTime = TriggerUtils.computeEndTimeToAllowParticularNumberOfFirings(getCronTriggerImpl(), null, Integer.parseInt(params[1]));
            } else if (Objects.equals(END_DATE_TYPE_BY, params[0])) {  //指定结束时间
                endDateTime = FORMAT_DATE.parse(params[1]);
            }
        } catch (ParseException e) {
            logger.error("ParseException", e);
        } finally {
            return endDateTime;
        }
    }

    public Date getStartDateTime() {
        try {
            return FORMAT_FULL.parse(startDate + " " + startTime + ":00");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getSpecify() {
        return specify;
    }

    public void setSpecify(String specify) {
        this.specify = specify;
    }

    public CategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(CategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getName() {
        return this.categoryEnum + "." + this.getInstance();
    }
    /**
     * 获取Cron表达式
     * @return
     */
    public String getCronString() {
        if (Objects.nonNull(cronString))
            return cronString;

        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartDate());

        String week = cal.get(Calendar.DAY_OF_WEEK) + "";

        String[] dateSplit = startDate.split(DATE_SPLIT);
        String[] timeSplit = startTime.split(TIME_SPLIT);

        List<String> stack = new ArrayList<>(7);
        //初始化公式
        stack.add("00");
        stack.add(timeSplit[1]);
        stack.add(timeSplit[0]);
        stack.add(dateSplit[2]);
        stack.add(dateSplit[1]);
        stack.add(week);
        stack.add(dateSplit[0]);

        return calFinalCronString(stack);
    }

    /**
     *  展示下次执行时间(前20次)
     * @throws ParseException
     */
    public void getNextFireTime() throws ParseException {
        List<Date> list;

        if (Objects.isNull(this.endDate)) {//没有截止日期
            list = TriggerUtils.computeFireTimes(getCronTriggerImpl(),null, DEFAULT_NUM_TIMES);
        } else {// 有截止日期
            list = TriggerUtils.computeFireTimesBetween(getCronTriggerImpl(),null, getStartDateTime(), getEndDate());
        }

        for (Date date : list) {
            logger.info("fire at: {}", FORMAT_FULL.format(date));
        }
    }

    /**
     * 获取JobDataMap
     * @return
     */
    public Map getJobDateMap() {
        Map<String, Object> params = new HashMap<>(16);
        params.put("categoryEnum", this.categoryEnum.toString());
        params.put("instance", this.instance);
        return params;
    }

    private CronTriggerImpl getCronTriggerImpl() throws ParseException {
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        String cronExpress = getCronString();
        cronTriggerImpl.setCronExpression(cronExpress);
        return  cronTriggerImpl;
    }

    public String getDescription() {
//        每个星期四发生，生效时间：2018-6-21至2018-8-30，从17:00到17:30，共有11个事件

        StringBuilder sb = new StringBuilder();
        String intervalDescription = "";

        if (interval > 1) {
            intervalDescription = String.valueOf(interval);

            if (this.repeatsEnum == RepeatsEnum.WEEKLY || this.repeatsEnum == RepeatsEnum.MONTHLY) {
                intervalDescription += "个";
            }
        }

        switch (this.repeatsEnum) {
            case DAILY: //每天执行
                sb.append("每"+intervalDescription+"天发生");
                break;
            case WEEKLY: //每周
                sb.append("每"+intervalDescription+"周的");
                String[] days = this.specify.split(",");

                for (int i = 0; i < days.length; i++) {
                    if (i == days.length -1) {
                        sb.deleteCharAt(sb.length() - 1);
                        sb.append("和").append(getDayByIntValue(days[i]));
                    } else {
                        sb.append(getDayByIntValue(days[i])).append("，");
                    }
                }

                sb.append("发生");

                break;
            case MONTHLY: //每月
                sb.append("每"+intervalDescription+"月的");

                String[] params = this.specify.split(TIME_SPLIT);

                if (Objects.equals(SPECIFY_TYPE_DAY, params[0])) {
                    String[] days2 = params[1].split(",");
                    for (int i = 0; i < days2.length; i++) {
                        if (i == days2.length -1) {
                            sb.deleteCharAt(sb.length() - 1);
                            sb.append("和").append(days2[i]).append("号");
                        } else {
                            sb.append(days2[i]).append("号,");
                        }
                    }
                } else {
                    if (Objects.equals(SPECIFY_TYPE_WEEK, params[0])) {
                        String[] values = params[1].split("#");

                        if (values.length > 1) { // 3#2
                            sb.append("第").append(values[1]).append("周").append(getDayByIntValue(values[0]));
                        } else if(params[1].indexOf("L") > -1) { //2L
                                sb.append("最后一周的").append(getDayByIntValue(String.valueOf(params[1].charAt(0))));
                        } else {// 1,2
                            String[] days2 = values[0].split(",");
                            for (int i = 0; i < days2.length; i++) {
                                if (i == days2.length - 1) {
                                    sb.deleteCharAt(sb.length() - 1);
                                    sb.append("和").append(getDayByIntValue(days2[i]));
                                } else {
                                    sb.append(getDayByIntValue(days2[i])).append(",");
                                }
                            }
                        }
                    }
                }

                sb.append("发生");
                break;
            case YEARLY: //每年
                sb.append("每"+intervalDescription+"年的");
                sb.append(startDate.substring(5,7)).append("月")
                        .append(startDate.substring(8)).append("日");
                sb.append("发生");
                break;
            default:
                break;
        }

        sb.append("，生效时间：").append(startDate);

        if (Objects.nonNull(getEndDate())) {
            sb.append("至").append(FORMAT_DATE.format(getEndDate()));
        }

        sb.append("，从").append(startTime).append("到").append(endTime);

        //是否次数
        if(Objects.nonNull(endDate)) {
            String[] params = endDate.split(TIME_SPLIT);
            if (Objects.equals(END_DATE_TYPE_AFTER, params[0])) { // 次数
                sb.append("，共有"+params[1]+"个事件");
            }
        }




//        sb.append("，从17:00到17:30");

        description = sb.toString();

        logger.info("description: {}", description);

        return description;
    }

    private String getDayByIntValue(String value) {
        String text;
        switch (value) {
            case "1":
                text = "星期日";
                break;
            case "2":
                text = "星期一";
                break;
            case "3":
                text = "星期二";
                break;
            case "4":
                text = "星期三";
                break;
            case "5":
                text = "星期四";
                break;
            case "6":
                text = "星期五";
                break;
            case "7":
                text = "星期六";
                break;
            default:
                text = "未知日期";
                break;
        }
        return text;
    }

    private String calFinalCronString(List stack) {
        switch (this.repeatsEnum) {
            case DAILY: //每天执行
                stack.set(3, "*/" + this.interval);
                stack.set(4, "*");
                stack.set(5, "?");
                stack.set(6, "*");
                break;
            case WEEKLY: //每周
                stack.set(3, "?");
                stack.set(4, "*");
                stack.set(5, this.specify + "/" + this.interval);
                stack.set(6, "*");
                break;
            case MONTHLY: //每月
                //day： 1-7
                //weekend: 1,7
                //weekday: 2-6
                //specify = "day:1,2,3"
                //specify = "week:3#2"   //目前只支持这种简单的格式
                //specify = "week:1-7#1" //不支持这种语法
                //specify = "week:1,7#2" //不支持这种语法

                String[] params = this.specify.split(TIME_SPLIT);

                if (Objects.equals(SPECIFY_TYPE_DAY, params[0])) {
                    stack.set(3, params[1]);
                } else {
                    stack.set(3, "?");
                }


                stack.set(4, "*/" + this.interval);

                if (Objects.equals(SPECIFY_TYPE_WEEK, params[0])) {
                    stack.set(5, params[1]);
                } else {
                    stack.set(5, "?");
                }
                stack.set(6, "*");


                break;
            case YEARLY: //每年
                stack.set(5, "?");
                stack.set(6, "*/" + this.interval);
                break;
            default:
                break;
        }

        cronString = String.join(" ", stack);
        logger.info("cron express is: {}", cronString);
        return cronString;
    }
}