package com.example.demo;

import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;

import com.netflix.zuul.context.RequestContext;

public class RouteTimesFilter extends AbstractRouteFilter {

    public RouteTimesFilter(RouteLocator routeLocator, UrlPathHelper urlPathHelper) {
        super(routeLocator,urlPathHelper);
    }

    @Override
    public String filterType() {
		//可以根据业务要求，修改过滤器类型
        return "post";
    }

    @Override
    public int filterOrder() {
		//过滤器顺序
        return 0;
    }

    @Override
    public boolean shouldFilter() {
		//可以根据业务要求，过滤相关路由
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        Route route = route(ctx.getRequest());
		//获取到路由信息，就可以做想要做的事了
        System.out.println(route);
        return null;
    }
}