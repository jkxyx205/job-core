package com.yodean.job.core.dto;

import org.springframework.scheduling.quartz.QuartzJobBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by rick on 2018/3/23.
 */
public class ScheduleDetail implements Serializable {
    private String name;

    private String cronExpression;

    private String groupName;

    private Class<? extends QuartzJobBean> job;

    private Map jobProps = new HashMap<>();

    public String getName() {
        if (null == name) {
            this.name = UUID.randomUUID().toString();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTriggerName() {
        return "trigger-" + this.name;
    }

    public String getTriggerGroupName() {
        return "trigger-group-" + this.groupName;
    }



    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getGroupName() {
        if (groupName == null) {
            this.groupName = "normal-group";
        }
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Class<? extends QuartzJobBean> getJob() {
        return job;
    }

    public void setJob(Class<? extends QuartzJobBean> job) {
        this.job = job;
    }

    public Map getJobProps() {
        return jobProps;
    }

}
