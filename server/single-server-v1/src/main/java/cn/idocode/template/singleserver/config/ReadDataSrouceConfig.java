package cn.idocode.template.singleserver.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
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
//@MapperScan(basePackages = "com.tencent.yyb.qms.readdao", sqlSessionTemplateRef = "writeSqlSessionTemplate")
public class ReadDataSrouceConfig {

    //第二个ds2数据源
    @Bean("read1Datasource")
    @ConfigurationProperties(prefix = "spring.datasource.read1")
    public DataSource read1Datasource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean("read1SqlSessionFactory")
    public SqlSessionFactory read1SqlSessionFactory(@Qualifier("read1Datasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "read1TransactionManager")
    public DataSourceTransactionManager read1TransactionManager(@Qualifier("read1Datasource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "read1SqlSessionTemplate")
    public SqlSessionTemplate read1SqlSessionTemplate(@Qualifier("read1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
