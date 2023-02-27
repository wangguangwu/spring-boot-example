package com.wangguangwu.junit.controller;

import com.wangguangwu.junit.service.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@RequestMapping("hello")
@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @GetMapping("/wang")
    public String wang() {
        return "Hello World";
    }

    @GetMapping("/hello")
    public void hello() {
        helloService.hello();
    }
}
