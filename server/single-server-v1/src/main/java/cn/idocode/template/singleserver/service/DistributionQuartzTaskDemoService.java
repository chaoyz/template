package cn.idocode.template.singleserver.service;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

/**
 * @author levic
 * 2020/9/4 12:32 AM
 **/
@Slf4j
@Service
public class DistributionQuartzTaskDemoService implements Job {
    public static final String GROUP_KEY = "GROUP_KEY";

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // do task logic
        log.info("DistributionQuartzTaskDemoService execute process.......");
    }
}
