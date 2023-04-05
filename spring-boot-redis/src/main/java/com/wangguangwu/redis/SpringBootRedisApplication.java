package com.wangguangwu.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangguangwu
 */
@SpringBootApplication
public class SpringBootRedisApplication {

    public static void main(String[] args) {
        try {
            SpringApplication.run(SpringBootRedisApplication.class, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
