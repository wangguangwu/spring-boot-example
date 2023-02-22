package com.wangguangwu.logback.controller;

import com.wangguangwu.logback.service.WangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @author wangguangwu
 */
@Slf4j
@RequestMapping("wang")
@RestController
public class WangController {

    @Resource
    private WangService wangService;

    @GetMapping("/hello")
    public String hello() {
        // 打印不同级别的日志
        log.debug("controller--debug");
        log.info("controller--info");
        log.warn("controller--warn");
        log.error("controller--error");
        wangService.hello();
        return "Hello World";
    }
}


