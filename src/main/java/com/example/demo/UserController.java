package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

@Controller
@RestController
public class UserController {
	@Autowired
    RouteLocator routeLocator;
	
	@Autowired
    ApplicationEventPublisher publisher;
	
	@RequestMapping("/hello")
    public String hello(String name) {
        return "hello!"+"----"+name;
    }
	
    @RequestMapping("/getAllUser")
    public ArrayList<String> getAllUser(HttpServletRequest request) {
    	String t=request.getHeader("Hello");
    	System.out.println("====="+t);
        ArrayList<String> list = new ArrayList<>();
        list.add("Benjieming-A");
        list.add("Huangsi-A");
        list.add("Yangyi-A");
        return list;

    }
    
    @RequestMapping("/refresh")
    public void refresh() {
        List<Route> list=routeLocator.getRoutes();
        for(Route route:list){
        	String path=route.getPath();
        	String prefix=route.getPrefix();
        }
        RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
        publisher.publishEvent(routesRefreshedEvent);
    }
}