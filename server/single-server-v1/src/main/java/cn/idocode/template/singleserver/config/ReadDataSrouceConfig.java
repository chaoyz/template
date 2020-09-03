package cn.idocode.template.singleserver.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * 数据源配置，定义为读取数据源
 *
 * @author levicyang
 * 2020-07-08 21:06
 */
@Configuration
@MapperScan(basePackages = "cn.idocode.template.singleserver.repository.read", sqlSessionTemplateRef = "readSqlSessionTemplate")
public class ReadDataSrouceConfig {

    @Bean(name = "readDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDatasource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("readSqlSessionFactory")
    public SqlSessionFactory readSqlSessionFactory(@Qualifier("readDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "readTransactionManager")
    public DataSourceTransactionManager readTransactionManager(@Qualifier("readDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "readSqlSessionTemplate")
    public SqlSessionTemplate readSqlSessionTemplate(@Qualifier("readSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
