package site.fsyj.course.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author fsyj on 2022/5/29
 */
@Data
@EnableConfigurationProperties(WxConfig.class)
@ConfigurationProperties(prefix = "wx", ignoreInvalidFields = true)
public class WxConfig {
    private String appId;
    private String secret;

    private int expire = 1000 * 60 * 60;
}
