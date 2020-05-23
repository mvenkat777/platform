
package com.car.platform.config;

import javax.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;


@Configuration
@EnableConfigurationProperties(ApplicationProperties.class)
@EnableTransactionManagement
@EnableAsync
public class ApplicationConfig {
    
    @Autowired
    private ApplicationProperties appProperties;
    
    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {
        ServletRegistrationBean registration = new ServletRegistrationBean(dispatcherServlet);
        registration.addUrlMappings("/*");
        
        registration.setMultipartConfig(new MultipartConfigElement("", 2097152, 4194304, 0));
        registration.setAsyncSupported(true);
        return registration;
    }
}
