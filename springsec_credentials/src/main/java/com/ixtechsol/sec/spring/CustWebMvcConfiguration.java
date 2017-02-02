package com.ixtechsol.sec.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EnableWebMvc
public class CustWebMvcConfiguration extends WebMvcConfigurerAdapter {
	Logger logger = LoggerFactory.getLogger(CustWebMvcConfiguration.class);

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/login").setViewName("loginPage");
        registry.addViewController("/forgotPassword").setViewName("forgotPassword");
        registry.addViewController("/profile");
        registry.addViewController("/secured").setViewName("securedPage");
        
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/**").addResourceLocations("classpath:/public/css/","classpath:/static/css/");
		registry.addResourceHandler("/font-awesome/**").addResourceLocations("classpath:/public/font-awesome/");
		registry.addResourceHandler("/js/**").addResourceLocations("classpath:/public/js/");
		registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/fonts/");
	}
        
    
}