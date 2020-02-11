package com.example.demo.apiVersion;
import org.springframework.web.servlet.mvc.condition.RequestCondition;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
 
/**
 * create by jack 2018/8/19
 *
 * @auther jack
 * @date: 2018/8/19 10:57
 * @Description:
 */
public class ApiVersionCondition implements RequestCondition<ApiVersionCondition> {
    // 路径中版本的正则表达式匹配， 这里用 /v1.0的形式
	private static final Pattern VERSION_PREFIX_PATTERN = Pattern.compile("^\\S+/v([1-9][.][0-9])$");
    /**
     * api的版本
     */
    private double apiVersion;
 
    public ApiVersionCondition(double  apiVersion) {
        this.apiVersion = apiVersion;
    }
 
 
    /**
     * 将不同的筛选条件合并
     * @param apiVersionCondition
     * @return
     */
    @Override
    public ApiVersionCondition combine(ApiVersionCondition apiVersionCondition) {
        // 采用最后定义优先原则，则方法上的定义覆盖类上面的定义
        return new ApiVersionCondition(apiVersionCondition.getApiVersion());
    }
 
 
    /**
     * 根据request查找匹配到的筛选条件
     * @param httpServletRequest
     * @return
     */
    public ApiVersionCondition getMatchingCondition(HttpServletRequest request) {
        Matcher m = VERSION_PREFIX_PATTERN.matcher(request.getRequestURI());
        if(m.find()){
        	 Double version = Double.valueOf(m.group(1));
            if(version >= this.apiVersion)
            {
                return this;
            }
        }
        return null;
    }
 
    public double  getApiVersion() {
        return apiVersion;
    }

    @Override
    public int compareTo(ApiVersionCondition other, HttpServletRequest request) {
        // 优先匹配最新的版本号
        return Double.compare(other.getApiVersion(), this.apiVersion);
    }
}