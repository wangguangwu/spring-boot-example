package com.wangguangwu.standard.controller;

import com.wangguangwu.standard.anno.LessLog;
import com.wangguangwu.standard.exception.ServiceException;
import com.wangguangwu.standard.log.LogType;
import com.wangguangwu.standard.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@Slf4j
@RequestMapping("/hello")
@RestController
public class HelloController {

    @Resource
    private HelloService helloService;

    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/exception")
    public void exception() {
        throw new ServiceException();
    }

    @LessLog(type = LogType.URL)
    @GetMapping("/lessUrl")
    public String lessUrl() {
        return "lessUrl";
    }

    @LessLog(type = LogType.REQUEST)
    @GetMapping("/lessRequest")
    public String lessRequest() {
        return "lessRequest";
    }

    @LessLog(type = LogType.RESPONSE)
    @GetMapping("/lessResponse")
    public String lessResponse() {
        return "lessResponse";
    }

    @LessLog(type = LogType.ALL)
    @GetMapping("/lessAll")
    public String lessAll() {
        return "lessAll";
    }

    @LessLog(type = LogType.NONE)
    @GetMapping("/lessNone")
    public String lessNone() {
        return "lessNone";
    }

    @GetMapping("/hello")
    public void hello() {
        log.info("hello controller");
        log.error("hello controller");
        helloService.hello();
    }
}
