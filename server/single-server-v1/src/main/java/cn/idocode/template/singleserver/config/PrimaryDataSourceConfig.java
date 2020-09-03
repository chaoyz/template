package cn.idocode.template.singleserver.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 多数据源配置，读写分离数据源，此配置文件定义为主数据源配置
 *
 * @author levicyang
 * 2020-07-08 20:53
 */
@Configuration
@MapperScan(basePackages = "cn.idocode.template.singleserver.repository.write", sqlSessionTemplateRef = "writeSqlSessionTemplate")
public class PrimaryDataSourceConfig {

    @Primary
    @Bean(name = "writeDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Primary
    @Bean("writeSqlSessionFactory")
    public SqlSessionFactory writeSqlSessionFactory(@Qualifier("writeDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Primary
    @Bean(name = "writeTransactionManager")
    public DataSourceTransactionManager writeTransactionManager(@Qualifier("writeDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "writeSqlSessionTemplate")
    public SqlSessionTemplate writeSqlSessionTemplate(@Qualifier("writeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
