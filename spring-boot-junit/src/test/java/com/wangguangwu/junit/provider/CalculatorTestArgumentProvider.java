package com.wangguangwu.junit.provider;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

/**
 * 为测试方法提供参数
 *
 * @author wangguangwu
 */
public class CalculatorTestArgumentProvider implements ArgumentsProvider {
    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return Stream.of(
                Arguments.of(0, 0, 0),
                Arguments.of(0, 1, 1),
                Arguments.of(1, 0, 1),
                Arguments.of(1, 2, 3),
                Arguments.of(2, 1, 3),
                Arguments.of(3, 5, 8),
                Arguments.of(5, 3, 8)
        );
    }
}
