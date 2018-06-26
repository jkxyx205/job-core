package com.yodean.job.core.dto;


import java.util.Map;

/**
 * Created by rick on 6/20/18.
 */
public class JobExpress {
    /**
     * 分类
     */
    private CategoryEnum categoryEnum;

    /**
     * 重复类型
     */
    private RepeatsEnum repeatsEnum;

    /**
     * 间隔
     */
    private Integer interval;

    /**
     * 实例id
     */
    private String instance;

    /**
     * 开始日期
     *  20121022
     */
    private String startDate;

    /**
     * 结束日期
     * 1. null
     * 2. by:20151023
     * 3. after:3
     */
    private String endDate;

    /**
     * 开始执行时间
     * 23:23
     */
    private String startTime;

    /**
     * 参数
     */
    private String params;

    /**
     * 任务数据
     */
    private Map<String, String> jobDataMap;


    public JobExpress(CategoryEnum categoryEnum, String instance) {
        this.instance = instance;
        this.categoryEnum = categoryEnum;
    }

    public static enum RepeatsEnum {
        DAILY, WEEKLY, MONTHLY, YEARLY
    }

    public static enum CategoryEnum {
        TASK, MEETING, NOTE
    }

    public CategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(CategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
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

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Map<String, String> getJobDataMap() {
        return jobDataMap;
    }

    public void setJobDataMap(Map<String, String> jobDataMap) {
        this.jobDataMap = jobDataMap;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    /**
     * 获取JOB名称
     * @return
     */
    public String getName() {
        return this.categoryEnum + "." + this.getInstance();
    }
}