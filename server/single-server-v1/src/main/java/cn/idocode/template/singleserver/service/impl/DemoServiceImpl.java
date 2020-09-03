package cn.idocode.template.singleserver.service.impl;

import cn.idocode.template.singleserver.model.User;
import cn.idocode.template.singleserver.repository.read.UserReadMapper;
import cn.idocode.template.singleserver.repository.write.UserWriteMapper;
import cn.idocode.template.singleserver.service.DemoService;
import cn.idocode.template.singleserver.service.DistributionQuartzTaskDemoService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * 封装基本查询、插入操作的demo
 */
@Slf4j
@Service
public class DemoServiceImpl implements DemoService {

    @Autowired
    private UserReadMapper userReadMapper;
    @Autowired
    private UserWriteMapper userWriteMapper;
    @Autowired
    private SchedulerFactoryBean schedulerFactoryBean;

    @PostConstruct
    public void init() {
        String jobKeyStr = "DataHandleServiceImpl";
        TriggerKey triggerKey = new TriggerKey(jobKeyStr, DistributionQuartzTaskDemoService.GROUP_KEY);
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        try {
            Trigger trigger = scheduler.getTrigger(triggerKey);
            if (trigger == null) {
                JobKey jobKey = JobKey.jobKey(jobKeyStr, DistributionQuartzTaskDemoService.GROUP_KEY);
                JobDetail jobDetail = JobBuilder.newJob(DistributionQuartzTaskDemoService.class)
                        .withIdentity(jobKey)
                        .build();
                CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("* */5 * * * ?");
                trigger = TriggerBuilder.newTrigger()
                        .withIdentity(triggerKey)
                        .forJob(jobDetail)
                        .withSchedule(scheduleBuilder)
                        .startNow()
                        .build();
                scheduler.scheduleJob(jobDetail, trigger);
                scheduler.start();
            }
        } catch (SchedulerException e) {
            log.error("DataHandleServiceImpl 执行任务异常", e);
        }
    }

    @Override
    public String echo(String param) throws Exception {
        return param;
    }

    @Override
    public User getUser(String name) {
        return userReadMapper.findUserByName(name);
    }

    @Override
    public User saveUser(User user) {
        userWriteMapper.insertUser(user);
        return user;
    }
}
