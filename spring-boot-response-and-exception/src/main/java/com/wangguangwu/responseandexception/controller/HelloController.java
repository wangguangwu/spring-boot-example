package com.wangguangwu.responseandexception.controller;

import com.wangguangwu.responseandexception.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangguangwu
 */
@Slf4j
@RequestMapping("/hello")
@RestController
public class HelloController {

    @RequestMapping("/success")
    public String success() {
        return "success";
    }

    @RequestMapping("/exception")
    public void exception() {
        throw new ServiceException();
    }
}
