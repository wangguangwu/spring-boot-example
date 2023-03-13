package com.wangguangwu.xxljob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author wangguangwu
 */
@EnableAspectJAutoProxy
@SpringBootApplication
public class SpringBootXxlJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootXxlJobApplication.class, args);
    }

}
