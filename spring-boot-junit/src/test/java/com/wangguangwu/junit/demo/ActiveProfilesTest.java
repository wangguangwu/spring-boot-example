package com.wangguangwu.junit.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * @author wangguangwu
 */
@ActiveProfiles("prod")
@SpringBootTest
class ActiveProfilesTest {

    @Value("${hello}")
    private String hello;

    @Test
    void active() {
        // 在 Spring Boot 中，加载顺序：yml > yaml > properties
        Assertions.assertEquals("prod-properties", hello);
    }
}
