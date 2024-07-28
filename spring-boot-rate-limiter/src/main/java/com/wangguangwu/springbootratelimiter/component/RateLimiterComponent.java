package com.wangguangwu.springbootratelimiter.component;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.util.Objects;

/**
 * 限流器组件。
 * </p>
 *
 * @author wangguangwu
 */
@Component
@Slf4j
public class RateLimiterComponent {

    private RateLimiter rateLimiter;

    public RateLimiterComponent() {
        // 默认速率配置
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod(3)
                .timeoutDuration(Duration.ZERO)
                .build();
        this.rateLimiter = RateLimiter.of("default", config);
    }

    public void setRate(double permitsPerSecond) {
        // 更新速率配置
        RateLimiterConfig config = RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofSeconds(1))
                .limitForPeriod((int) permitsPerSecond)
                .timeoutDuration(Duration.ZERO)
                .build();
        this.rateLimiter = RateLimiter.of("custom", config);
    }

    public boolean tryAcquire(int timeout) {
        try {
            return rateLimiter.acquirePermission(timeout);
        } catch (Exception e) {
            log.error("Failed to acquire permission: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 降级处理
     */
    public Object fullback() {
        HttpServletResponse response = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getResponse();
        if (response != null) {
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            try (PrintWriter writer = response.getWriter()) {
                log.info("服务出错，请稍后重试");
                writer.println("服务出错，请稍后重试");
                writer.flush();
            } catch (IOException e) {
                log.error("服务降级: {}", e.getMessage(), e);
            }
        }
        return null;
    }
}
