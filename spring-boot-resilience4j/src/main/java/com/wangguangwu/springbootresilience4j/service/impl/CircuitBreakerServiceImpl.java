package com.wangguangwu.springbootresilience4j.service.impl;

import com.wangguangwu.springbootresilience4j.service.CircuitBreakerService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 熔断器。
 * <p>
 * 监控方法调用的成功和失败比例，并在一定条件下打开熔断器，阻止后续调用。
 *
 * @author wangguangwu
 */
@Service
@Slf4j
public class CircuitBreakerServiceImpl implements CircuitBreakerService {

    @Override
    @CircuitBreaker(name = "backendA", fallbackMethod = "fallback")
    public String doSomething() {
        // 业务逻辑
        if (Math.random() < 0.5) {
            throw new RuntimeException("Failed");
        }
        return "Success";
    }

    public String fallback(Throwable t) {
        return "Fallback response due to exception: " + t.getMessage();
    }
}
