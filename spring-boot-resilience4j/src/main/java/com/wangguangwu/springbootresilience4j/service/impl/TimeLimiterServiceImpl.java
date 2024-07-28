package com.wangguangwu.springbootresilience4j.service.impl;

import com.wangguangwu.springbootresilience4j.service.TimeLimiterService;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * 限时器。
 * <p>
 * 限制方法调用的执行时间。
 *
 * @author wangguangwu
 */
@Service
public class TimeLimiterServiceImpl implements TimeLimiterService {

    @Override
    @TimeLimiter(name = "backendD")
    public CompletableFuture<String> doSomething() {
        // 可能需要长时间运行的逻辑
        return CompletableFuture.completedFuture("Success!");
    }
}
