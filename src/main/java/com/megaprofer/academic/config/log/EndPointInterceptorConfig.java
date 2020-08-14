package com.megaprofer.academic.config.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class EndPointInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private EndPointInterceptor endPointInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.endPointInterceptor);
    }
}
