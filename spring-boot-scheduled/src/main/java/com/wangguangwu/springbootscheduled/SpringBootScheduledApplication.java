package com.wangguangwu.springbootscheduled;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务
 *
 * @author wangguangwu
 */
@EnableScheduling
@SpringBootApplication
public class SpringBootScheduledApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootScheduledApplication.class, args);
    }

}
