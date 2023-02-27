package com.wangguangwu.junit.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author wangguangwu
 */
@SpringBootTest
class InnerTest {

    @Test
    void hello() {
        System.out.println("Hello");
    }
}
