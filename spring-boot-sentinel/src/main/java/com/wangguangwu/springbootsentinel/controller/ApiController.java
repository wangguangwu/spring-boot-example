package com.wangguangwu.springbootsentinel.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangguangwu
 */
@RestController
@RequestMapping("/api")
@Slf4j
public class ApiController {

    @GetMapping("/sentinel")
    @SentinelResource(value = "sentinelResource", blockHandler = "handleBlock")
    public String sentinel() {
        log.info("测试sentinel");
        return "Hello, Sentinel!";
    }

    public String handleBlock(BlockException ex) {
        log.error("执行报错：{}", ex.getMessage(), ex);
        return "Request has been blocked by Sentinel: " + ex.getMessage();
    }
}
