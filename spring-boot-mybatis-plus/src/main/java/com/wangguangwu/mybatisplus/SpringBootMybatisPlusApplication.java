package com.wangguangwu.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * `@MapperScan` 用于扫描 Mapper 接口所在的包，将 Mapper 接口注册到 Spring 容器中
 * `@EnableTransactionManagement` 用于开启Spring的事务管理功能
 *
 * @author wangguangwu
 */
@MapperScan("com.wangguangwu.mybatisplus.mapper")
@EnableTransactionManagement
@SpringBootApplication
public class SpringBootMybatisPlusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisPlusApplication.class, args);
    }

}
