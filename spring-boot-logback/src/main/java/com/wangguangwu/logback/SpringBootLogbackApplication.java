package com.wangguangwu.logback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author wangguangwu
 */
@EnableScheduling
@SpringBootApplication
public class SpringBootLogbackApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootLogbackApplication.class, args);
    }
}
