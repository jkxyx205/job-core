package com.yodean.job.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by rick on 2018/3/23.
 */
@DisallowConcurrentExecution
public class SampleJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(SampleJob.class);


    @Override
    protected void executeInternal(JobExecutionContext context)
            throws JobExecutionException {
        StringBuilder sb = new StringBuilder();

        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        for (Map.Entry<String, Object>  entry : dataMap.entrySet()) {
            sb.append("name: ").append(entry.getKey()).append(", value").append(": ").append(entry.getValue()).append("\r\n");
        }

        logger.info("SampleJob execute at time: {}, the DataMap is {}", new SimpleDateFormat("HH:mm:ss").format(new Date())
                , sb.toString());

    }

}
