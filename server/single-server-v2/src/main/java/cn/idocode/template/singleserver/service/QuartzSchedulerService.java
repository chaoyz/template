package cn.idocode.template.singleserver.service;

import cn.idocode.template.singleserver.exception.ServiceException;
import cn.idocode.template.singleserver.model.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 分布式定时任务demo
 *
 * @author levic
 * 2020/7/12 9:03 PM
 **/
@Slf4j
@Service
public class QuartzSchedulerService {

    @Autowired
    @Qualifier("schedulerFactory")
    private Scheduler scheduler;

    public void startJob(JobDetail jobDetail, Trigger trigger) throws ServiceException, SchedulerException {
        if (jobDetail == null || trigger == null) {
            throw new ServiceException(ResultCode.PARAM_ERROR);
        }
        Date date = scheduler.scheduleJob(jobDetail, trigger);
        log.info("start schedule task time:{}", date);
    }
}
