package com.example.demo.rateLimiter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
 
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurationSupport {
	@Autowired
	private RateLimitCheckInterceptor rateLimitCheckInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(rateLimitCheckInterceptor).addPathPatterns("/**");
		super.addInterceptors(registry);
	}
}