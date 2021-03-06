package cn.idocode.template.singleserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;
import java.util.Properties;

/**
 * @author levic
 * 2018/7/12 15:32
 */
@Configuration
public class QuartzConfigration {

    @Value("${spring.datasource.write.url}")
    private String dbUrl;

    @Value("${spring.datasource.write.username}")
    private String username;

    @Value("${spring.datasource.write.password}")
    private String password;

    @Value("${spring.datasource.write.driver-class-name}")
    private String driverClassName;

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        //设置是否任意一个已定义的Job会覆盖现在的Job。默认为false，即已定义的Job不会覆盖现有的Job。
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动5秒后  ，定时器才开始启动
        bean.setStartupDelay(5);
        try {
            bean.setQuartzProperties(quartzProperties());
        } catch (IOException e) {
            e.printStackTrace();
        }

        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        bean.setJobFactory(jobFactory);
        return bean;
    }


    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        Properties properties = new Properties();
        properties.put("org.quartz.scheduler.instanceName", "WorkflowQuartzScheduler");
        properties.put("org.quartz.scheduler.instanceId", "AUTO");
        properties.put("org.quartz.scheduler.rmi.export", "false");
        properties.put("org.quartz.scheduler.rmi.proxy", "false");
        properties.put("org.quartz.scheduler.wrapJobExecutionInUserTransaction", "false");
        properties.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        properties.put("org.quartz.threadPool.threadCount", "30");
        properties.put("org.quartz.threadPool.threadPriority", "5");
        properties.put("org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread", "true");
        properties.put("org.quartz.jobStore.misfireThreshold", "60000");
        properties.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        properties.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.StdJDBCDelegate");
        properties.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        properties.put("org.quartz.jobStore.dataSource", "qzDS");
        properties.put("org.quartz.jobStore.isClustered", "true");
        properties.put("org.quartz.dataSource.qzDS.driver", driverClassName);
        properties.put("org.quartz.dataSource.qzDS.URL", dbUrl);
        properties.put("org.quartz.dataSource.qzDS.user", username);
        properties.put("org.quartz.dataSource.qzDS.password", password);
        properties.put("org.quartz.dataSource.qzDS.maxConnections", "200");
        propertiesFactoryBean.setProperties(properties);
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }
}
