package com.example.demo.rateLimiter;

import java.util.concurrent.TimeUnit;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
@RequestMapping("/apis")
public class RateController {
  
  @RateLimiter(base = RateLimiter.Base.General, permits = 2, timeUnit = TimeUnit.MINUTES)
  @GetMapping("/tests")
  public String test() {
	  return "test!";
  }
  
  @RateLimiter(base = RateLimiter.Base.IP, path="/testforid", permits = 4, timeUnit = TimeUnit.MINUTES)
  @GetMapping("/testforid/{id}")
  public String testforid(@PathVariable Long id) {
	  return "test! " + id;
  }
 
}