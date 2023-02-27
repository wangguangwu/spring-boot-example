package com.wangguangwu.junit.demo;

import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
@DisplayName("基本测试类")
class BaseTest {

    //========================== 非测试方法 ==========================

    @BeforeAll
    public static void beforeAll() {
        System.out.println("@BeforeAll 在所有方法执行前执行，只执行一次");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("@AfterAll 在所有方法执行后执行，只执行一次");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("@BeforeEach 在每个测试方法执行前执行");
    }

    @AfterEach
    public void afterEach() {
        System.out.println("@BeforeEach 在每个测试方法执后前执行");
    }

    //========================== 非测试方法 ==========================

    @DisplayName("hello")
    @Test
    void hello() {
        System.out.println("@Test 测试方法");
    }

    @Timeout(value = 100, unit = TimeUnit.MICROSECONDS)
    @Test
    void timeout() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            // ignore
            System.out.println("@Timeout 当测试方法运行超过了指定时间会返回错误");
        }
    }

    @Disabled("测试 @Disabled 注解")
    @Test
    void disabled() {
        System.out.println("@Disabled 测试时会跳过");
    }

    @Ignore
    @Test
    void ignore() {
        System.out.println("@Ignore 暂不执行该方法");
    }
}
