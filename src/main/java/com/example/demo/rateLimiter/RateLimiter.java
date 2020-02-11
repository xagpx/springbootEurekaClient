package com.example.demo.rateLimiter;


import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
 
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
 
@Retention(RUNTIME)
@Target({ TYPE, METHOD })
public @interface RateLimiter {
 
	public enum Base {
		General, //统一控制 
		IP, 	 //按IP限制
		User     //按用户控制
	};
 
	Base base();
	
	String path() default "";
	
	TimeUnit timeUnit() default TimeUnit.SECONDS;
 
	int permits();
}