package cn.idocode.template.singleserver.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * web项目filter设置
 *
 * @author levicyang
 */
@Configuration
public class WebFilterConfig {

    /**
     * 设置登录filter
     */
//    @Bean
    public FilterRegistrationBean<?> registLoginFilter() {
        FilterRegistrationBean<?> registration = new FilterRegistrationBean<>();
        // TODO add some filters
        return registration;
    }
}
