package com.wangguangwu.junit;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author wangguangwu
 */
@MapperScan("com.wangguangwu.junit.mapper")
@SpringBootApplication
public class SpringBootJunitApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootJunitApplication.class, args);
    }
}
