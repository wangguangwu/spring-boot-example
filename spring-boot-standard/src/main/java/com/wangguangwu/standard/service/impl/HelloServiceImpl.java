package com.wangguangwu.standard.service.impl;

import com.wangguangwu.standard.service.HelloService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangguangwu
 */
@Slf4j
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello() {
        log.info("hello service");
        log.error("hello service");
    }
}
