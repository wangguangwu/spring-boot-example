package com.wangguangwu.springbootresilience4j.service.impl;

import com.wangguangwu.springbootresilience4j.service.RateLimiterService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.stereotype.Service;

/**
 * 限流器。
 * <p>
 * 限制方法调用的速率，防止服务过载。
 *
 * @author wangguangwu
 */
@Service
public class RateLimiterServiceImpl implements RateLimiterService {

    @Override
    @RateLimiter(name = "backendB")
    public String doSomething() {
        // 受限流器保护的方法逻辑
        return "Success!";
    }
}
