package com.tutor.tutor_platform.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    
    @GetMapping("/")
    public String home() {
        return "Tutor Platform 运行成功！";
    }
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, 项目正常工作！";
    }
}