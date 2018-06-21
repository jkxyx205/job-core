package com.yodean.job.core.controller;

import com.yodean.job.core.dto.ScheduleDetail;
import com.yodean.job.core.service.ScheduleService;
import com.yodean.job.job.SampleJob;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by rick on 6/11/18.
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * 添加定时任务
     */
    @GetMapping("/add")
    public String addCron() throws SchedulerException {

        ScheduleDetail scheduleDetail = new ScheduleDetail();

        scheduleDetail.setName("test");
        scheduleDetail.setGroupName("group-rick");
        scheduleDetail.setJob(SampleJob.class);
        scheduleDetail.setCronExpression("0/10 * * * * ? ");

        scheduleDetail.getJobProps().put("id", "12");


        scheduleService.addJob(scheduleDetail);

        return "add cron";
    }

    /**
     * 删除定时任务
     */
    @GetMapping("/del")
    public String delCron() throws SchedulerException {
        scheduleService.deleteJob("test", "group-rick");
        return "del cron";
    }

    /**
     * 修改定时任务
     */
    @GetMapping("/update")
    public String updateCron() throws SchedulerException {
        ScheduleDetail scheduleDetail = new ScheduleDetail();


        scheduleDetail.setName("test");
        scheduleDetail.setGroupName("group-rick");

//        scheduleDetail.getJobProps().put("name", "Ashley"); 修改无效
        scheduleDetail.setCronExpression("0/30 * * * * ? ");

        //schedule.setJob(SampleJob.class);

        scheduleService.updateJob(scheduleDetail);

        return "update cron";
    }

    /***
     * 暂停任务
     * @return
     */
    @GetMapping("/pause")
    public String pauseJob() throws SchedulerException {
        scheduleService.pauseJob("test", "group-rick");
        return "pauseJob";
    }

    /**
     * 恢复任务
     */
    @GetMapping("/resume")
    public String resumeJob() throws SchedulerException {
        scheduleService.resumeJob("test", "group-rick");
        return "resumeJob";
    }

    /**
     * 立即执行
     */
    @GetMapping("/execute")
    public String execute() throws SchedulerException {
        scheduleService.executeOnceJob("test", "group-rick");
        return "executeOnceJob";
    }

    @GetMapping("/list")
    public List<JobKey> list() throws SchedulerException {
        return scheduleService.listAllJobs();
    }
}
