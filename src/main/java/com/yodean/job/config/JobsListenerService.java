package com.yodean.job.config;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by rick on 2018/3/23.
 */

@Service
public class JobsListenerService implements JobListener {
    private final Logger logger = LoggerFactory.getLogger(JobsListenerService.class);

    @Override
    public String getName() {
        return "Main Listener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
//        logger.info("Job to be executed " + context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
//        logger.info("Job execution vetoed " + context.getJobDetail().getKey().getName());
    }

    @Override
    public void jobWasExecuted(
            JobExecutionContext context, JobExecutionException jobException
    ) {
//        logger.info(
//                "Job was executed " +
//                        context.getJobDetail().getKey().getName() +
//                        (jobException != null ? ", with error" : "")
//        );
    }
}