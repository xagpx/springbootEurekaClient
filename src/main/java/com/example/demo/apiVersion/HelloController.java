package com.example.demo.apiVersion;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口版本测试类
 */
@RestController
@RequestMapping("/api")
public class HelloController {

	@ApiVersion(1.0)
	@GetMapping("/test/v1{version}")
	public String test1() {
		return "Hello World! version 1";
	}

	@ApiVersion(1.1)
	@GetMapping("/test/v2{version}")
	public String test3() {
		return "Hello World! version 1.1";
	}

	@ApiVersion(2.0)
	@GetMapping("/test/v22{version}")
	public String test2() {
		return "Hello World! version 2";
	}
}