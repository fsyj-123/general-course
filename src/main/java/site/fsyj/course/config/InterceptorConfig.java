package site.fsyj.course.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.fsyj.course.handler.AuthenticationFilter;

import javax.annotation.Resource;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Resource
    private AuthenticationFilter authenticationFilter;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authenticationFilter).addPathPatterns("/api/**");
    }
}
