package cn.idocode.template.singleserver.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 项目自定义配置信息，本地配置
 *
 * @author levic
 * 2020/9/4 12:23 AM
 **/
@Data
@Configuration
@ConfigurationProperties(prefix = "project")
public class ProjectConfig {
    private boolean debugLog = true;
}
