package com.example.demo.rateLimiter;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
 
@Component
public class RateLimitCheckInterceptor implements HandlerInterceptor {
	
	private static final String[] IP_HEADER_KYES = { 
			"X-Forwarded-For", 
			"X-Real-IP", 
			"Proxy-Client-IP",
			"WL-Proxy-Client-IP", 
			"HTTP_CLIENT_IP", 
			"HTTP_X_FORWARDED_FOR" };
	
	private static final String USER_TOKEN_KEY = "UserToken";
	
	@Autowired
	private RedisRateLimiterFactory redisRateLimiterFactory;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		boolean isSuccess = true;
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		if (method.isAnnotationPresent(RateLimiter.class)) {
			RateLimiter rateLimiterAnnotation = method.getAnnotation(RateLimiter.class);
			int permits = rateLimiterAnnotation.permits();
			TimeUnit timeUnit = rateLimiterAnnotation.timeUnit();
			String path = rateLimiterAnnotation.path();
			if ("".equals(path)) {
				path = request.getRequestURI();
			}
 
			if (rateLimiterAnnotation.base() == RateLimiter.Base.General) {
				String rateLimiterKey = path;
				RedisRateLimiter redisRatelimiter = redisRateLimiterFactory.get(path, timeUnit,permits);
				isSuccess = rateCheck(redisRatelimiter, rateLimiterKey, response);
			} else if (rateLimiterAnnotation.base() == RateLimiter.Base.IP) {
				String ip = getIP(request);
				if (ip != null) {
					String rateLimiterKey = path + ":" + ip;
					RedisRateLimiter redisRatelimiter = redisRateLimiterFactory.get(rateLimiterKey, timeUnit,permits);
					isSuccess =	rateCheck(redisRatelimiter, rateLimiterKey, response);
				}
			} else if (rateLimiterAnnotation.base() == RateLimiter.Base.User) {
				String userToken = getUserToken(request);
				if (userToken != null) {
					String rateLimiterKey = path + ":" + userToken;
					RedisRateLimiter redisRatelimiter = redisRateLimiterFactory.get(rateLimiterKey, timeUnit,permits);
					isSuccess =rateCheck(redisRatelimiter, rateLimiterKey, response);
				}
			}
			
		}
		return isSuccess;
	}
 
	
	
	private boolean rateCheck(RedisRateLimiter redisRatelimiter, String keyPrefix, HttpServletResponse response)throws Exception {
		if (!redisRatelimiter.acquire(keyPrefix)) {
			JSONObject json=new JSONObject();
		    json.put("status", HttpStatus.TOO_MANY_REQUESTS.value());
		    json.put("message", "Too Many Requests!");
			response.setCharacterEncoding("UTF-8");  
		    response.setContentType("application/json; charset=utf-8");  
			response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
			response.getWriter().print(json.toString());
			return false;
		}
		return true;
	}
	private String getIP(HttpServletRequest request) {
		for (String ipHeaderKey : IP_HEADER_KYES) {
			String ip = request.getHeader(ipHeaderKey);
			if (ip != null && ip.length() != 0 && (!"unknown".equalsIgnoreCase(ip))) {
				return ip;
			}
			else {
				return request.getRemoteHost();
			}
		}
		return null;
	}
	
	private String getUserToken(HttpServletRequest request) {
		String userToken = request.getHeader(USER_TOKEN_KEY);
		return userToken;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
 
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}
	
}