package cn.idocode.template.singleserver;

import cn.idocode.template.singleserver.config.ProjectConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
@EnableConfigurationProperties(value = {ProjectConfig.class})
@ComponentScan({"cn.idocode.template.singleserver"})
public class SingleserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingleserverApplication.class, args);
	}

}
