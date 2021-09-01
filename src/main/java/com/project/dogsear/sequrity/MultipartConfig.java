package com.project.dogsear.sequrity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Configuration
public class MultipartConfig {
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }
}
