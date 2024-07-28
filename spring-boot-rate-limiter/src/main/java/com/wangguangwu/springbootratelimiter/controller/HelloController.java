package com.wangguangwu.springbootratelimiter.controller;

import com.wangguangwu.springbootratelimiter.annotation.MyRateLimiter;
import com.wangguangwu.springbootratelimiter.service.MessageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangguangwu
 */
@RestController
@Slf4j
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private MessageService messageService;

    /**
     * 通过注解实现限流
     */
    @MyRateLimiter(rate = 1.0, timeout = 500)
    @GetMapping("/message")
    public String message() {
        String result = messageService.sendMessage("Hello World") ? "发送消息成功" : "发送消息失败，请重试";
        log.info(result);
        return result;
    }
}
