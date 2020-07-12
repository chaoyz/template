package cn.idocode.template.singleserver.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 多数据源配置，读写分离数据源，此配置文件定义为主数据源配置
 *
 * @author levicyang
 * 2020-07-08 20:53
 */
@Configuration
@EnableJpaRepositories(basePackages = "cn.idocode.template.singleserver.repository.repo1",
        entityManagerFactoryRef = "localContainerEntityManagerFactoryBeanWrite",
        transactionManagerRef = "platformTransactionManagerWrite")
public class PrimaryDataSourceConfig {

    @Autowired
    private DataSource writeDatasource;
    @Autowired
    private JpaProperties jpaProperties;

    @Bean("writeDatasource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBeanWrite(EntityManagerFactoryBuilder builder) {
        return builder.dataSource(writeDatasource)
                .properties(jpaProperties.getProperties())
                .packages("cn.idocode.template.singleserver.model") // 注意这里设置包路径
                .persistenceUnit("persistenceUnit-write")
                .build();
    }

    @Bean
    @Primary
    public PlatformTransactionManager platformTransactionManagerWrite(EntityManagerFactoryBuilder builder) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = localContainerEntityManagerFactoryBeanWrite(builder);
        return new JpaTransactionManager(localContainerEntityManagerFactoryBean.getObject());
    }

}
