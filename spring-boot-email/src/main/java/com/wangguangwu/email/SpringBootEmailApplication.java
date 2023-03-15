package com.wangguangwu.email;

import com.wangguangwu.email.properties.EmailConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(EmailConfigProperties.class)
@SpringBootApplication
public class SpringBootEmailApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootEmailApplication.class, args);
    }

}
