package com.wangguangwu.springbootresilience4j.controller;

import com.wangguangwu.springbootresilience4j.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

/**
 * @author wangguangwu
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MyController {

    private final CircuitBreakerService circuitBreakerService;
    private final RateLimiterService rateLimiterService;
    private final RetryService retryService;
    private final TimeLimiterService timeLimiterService;
    private final BulkheadService bulkheadService;

    @GetMapping("/circuitbreaker")
    public String circuitBreaker() {
        return circuitBreakerService.doSomething();
    }

    @GetMapping("/ratelimiter")
    public String rateLimiter() {
        return rateLimiterService.doSomething();
    }

    @GetMapping("/retry")
    public String retry() {
        return retryService.doSomething();
    }

    @GetMapping("/timelimiter")
    public CompletableFuture<String> timeLimiter() {
        return timeLimiterService.doSomething();
    }

    @GetMapping("/bulkhead")
    public String bulkhead() {
        return bulkheadService.doSomething();
    }
}
