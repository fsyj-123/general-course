package site.fsyj.course.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(ApiConfig.class)
@ConfigurationProperties(prefix = "api")
@Data
public class ApiConfig {
    /**
     * 课程表解析API
     */
    private String course;
}
