package com.yodean.job.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * Created by rick on 6/14/18.
 * 提醒JOB
 */
public class AlertJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(AlertJob.class);

    private String categoryEnum;

    private String instance;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("produce message to {}#{}", categoryEnum, instance);
    }

    public String getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(String categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }
}
