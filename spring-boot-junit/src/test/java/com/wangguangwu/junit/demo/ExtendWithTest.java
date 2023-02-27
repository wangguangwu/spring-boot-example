package com.wangguangwu.junit.demo;

import com.wangguangwu.junit.controller.HelloController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

/**
 * @author wangguangwu
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExtendWithTest {

    @Resource
    private HelloController helloController;

    @Test
    void hello() {
        helloController.hello();
        System.out.println("Hello");
    }
}
