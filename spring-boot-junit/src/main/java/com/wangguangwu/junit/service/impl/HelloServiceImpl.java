package com.wangguangwu.junit.service.impl;

import com.wangguangwu.junit.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author wangguangwu
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello() {
        System.out.println("Hello");
    }
}
