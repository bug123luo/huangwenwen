package com.lcclovehww.springboot.chapter2.main;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@EnableAutoConfiguration
public class Chapter1Application {

	@RequestMapping("/test")
	@ResponseBody
	public Map<String, String> test(){
		Map<String, String> map = new HashMap<String, String>();
		map.put("key", "value");
		return map;
	}

	public static void main(String[] args) {
		SpringApplication.run(Chapter1Application.class, args);
	}
}
