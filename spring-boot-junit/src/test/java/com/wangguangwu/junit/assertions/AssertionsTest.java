package com.wangguangwu.junit.assertions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
class AssertionsTest {

    @Test
    void assertEquals() {
        // 判断是否相等
        int num = 3 + 5;
        Assertions.assertEquals(8, num);

        double result = 10.0 / 3;
        // 判断计算值是否在浮点数的指定范围内上下浮动
        // message 参数可以不传
        Assertions.assertEquals(3, result, 0.5, "计算数值偏差较大");
    }

    @Test
    void assertSame() {
        Object o1 = new Object();
        Object o2 = o1;
        Object o3 = new Object();
        // 直接打印地址看看
        System.out.println(o1);
        System.out.println(o2);
        System.out.println(o3);

        // 判断两个对象是否是同一个
        Assertions.assertSame(o1, o2);
        Assertions.assertNotSame(o2, o3);
        Assertions.assertNotSame(o1, o3);
    }

    @Test
    void assertArrayEquals() {
        String[] arr1 = {"aa", "bb"};
        String[] arr2 = {"aa", "bb"};
        String[] arr3 = {"bb", "aa"};

        // 判断两个数组的元素是否完全相同
        Assertions.assertArrayEquals(arr1, arr2);
        Assertions.assertArrayEquals(arr1, arr3, "两个数组不相同");
    }

    @Test
    void combination() {
        // 组合条件断言
        // 一条断言中组合多个断言，要求这些断言同时、全部通过，则外部的组合断言才能通过
        Assertions.assertAll(
                () -> {
                    int num = 3 + 5;
                    Assertions.assertEquals(8, num);
                },
                () -> {
                    String[] arr1 = {"aa", "bb"};
                    String[] arr2 = {"aa", "bb"};
                    Assertions.assertArrayEquals(arr1, arr2);
                }
        );
    }

    @Test
    void exception() {
        // 异常抛出断言
        // 运行必定会抛出指定异常，如果没有抛出异常则断言失败
        Assertions.assertThrows(ArithmeticException.class,
                () -> {
                    int i = 1 / 0;
                });
    }

    @Test
    void timeout() {
        // 执行超时断言
        Assertions.assertTimeout(Duration.ofMillis(500),
                () -> {
                    System.out.println("begin ...");
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("end ...");
                });
    }

    @Test
    void fail() {
        if (ZonedDateTime.now().getHour() < 25) {
            Assertions.fail();
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void assumptions(int value) {
        // 前置条件检查机制
        // 如果一个单元测试的前置条件不满足，则当前的测试会被跳过，后续的测试不会执行
        Assumptions.assumeTrue(value > 5);
        Assertions.assertTrue(value > 0);
    }

    @Test
    void argumentInject(TestInfo testInfo) {
        // 使用 TestInfo 可以获取当前单元测试的类名、方法名等参数
        Class<?> aClass = testInfo.getTestClass().get();
        System.out.println(aClass.getName());
    }
}
