package com.wangguangwu.junit.demo;

import com.wangguangwu.junit.enums.Gender;
import com.wangguangwu.junit.provider.CalculatorTestArgumentProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author wangguangwu
 */
@DisplayName("ParameterizedTest")
class ParameterizedExampleTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void valueSource(int value) {
        // 指定一个字符串、基本类型或枚举类型的数组作为测试参数
        Assertions.assertTrue(value > 0 && value < 4);
    }

    @ParameterizedTest
    @EnumSource(value = Gender.class, names = {"MALE", "FEMALE"})
    void enumSource(Gender gender) {
        // 指定一个枚举类型的数组作为测试参数
        Assertions.assertTrue(gender == Gender.MALE || gender == Gender.FEMALE);
    }

    @ParameterizedTest
    @MethodSource({"dataProvider1", "dataProvider2"})
    void dataStreamParameterized(Integer value) {
        // 指定一个静态方法或实例方法返回一个 Stream、Iterable、Iterator 或参数数组，作为测试参数
        Assertions.assertTrue(value < 24);
    }

    @ParameterizedTest
    @CsvSource({"1,One", "2,Two", "3,Three"})
    void vcsSource(int argument, String expected) {
        // 指定一个或多个用逗号分隔的字符串，将其解析为一个或多个测试参数
        String result;
        switch (argument) {
            case 1:
                result = "One";
                break;
            case 2:
                result = "Two";
                break;
            case 3:
                result = "Three";
                break;
            default:
                result = "Invalid argument";
                break;
        }
        Assertions.assertEquals(expected, result);
    }

    @ParameterizedTest
    @ArgumentsSource(CalculatorTestArgumentProvider.class)
    void testAdd(int a, int b, int sum) {
        // 指定一个 ArgumentsProvider 的实现类，从其提供的测试参数中获取数据
        Assertions.assertEquals(sum, a + b);
    }

    //=====================私有方法========================

    private static List<Integer> dataProvider1() {
        // 必须是一个静态方法
        return Arrays.asList(1, 2, 3, 4, 5);
    }

    private static Stream<Integer> dataProvider2() {
        // 必须是一个静态方法
        return Stream.of(6, 7, 8, 9, 10);
    }
}
