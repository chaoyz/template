package cn.idocode.template.singleserver.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源配置，定义为读取数据源
 *
 * @author levicyang
 * 2020-07-08 21:06
 */
@Configuration
@EnableJpaRepositories(basePackages = "cn.idocode.template.singleserver.repository.repo2",
        entityManagerFactoryRef = "localContainerEntityManagerFactoryBeanRead",
        transactionManagerRef = "platformTransactionManagerRead")
public class ReadDataSrouceConfig {

    @Autowired
    private DataSource read1Datasource;
    @Autowired
    private JpaProperties jpaProperties;

    //第二个ds2数据源
    @Bean("read1Datasource")
    @ConfigurationProperties(prefix = "spring.datasource.read1")
    public DataSource read1Datasource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("localContainerEntityManagerFactoryBeanRead")
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBeanRead(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(read1Datasource)
                .properties(jpaProperties.getProperties())
                .packages("cn.idocode.template.singleserver.model") // 注意这里设置包路径
                .persistenceUnit("persistenceUnit-write")
                .build();
    }

    @Bean
    public PlatformTransactionManager platformTransactionManagerRead(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = localContainerEntityManagerFactoryBeanRead(builder);
        return new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
    }
}
