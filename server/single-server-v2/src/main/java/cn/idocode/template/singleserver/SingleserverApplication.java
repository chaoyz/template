package cn.idocode.template.singleserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class SingleserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(SingleserverApplication.class, args);
	}

}
