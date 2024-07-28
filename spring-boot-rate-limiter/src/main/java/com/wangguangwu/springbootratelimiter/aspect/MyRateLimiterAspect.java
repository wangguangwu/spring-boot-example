package com.wangguangwu.springbootratelimiter.aspect;

import com.wangguangwu.springbootratelimiter.annotation.MyRateLimiter;
import com.wangguangwu.springbootratelimiter.component.RateLimiterComponent;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 实现自定义限流注解的切面。
 * <p>
 * 这个切面类使用 {@link RateLimiterComponent} 来控制请求的速率。
 * 如果请求的速率超过了限制，则会抛出 {@link RuntimeException}。
 * </p>
 *
 * @author wangguangwu
 * @see RateLimiterComponent
 * @see MyRateLimiter
 */
@Aspect
@Component
@Slf4j
public class MyRateLimiterAspect {

    @Resource
    private RateLimiterComponent rateLimiterComponent;

    @Pointcut("@annotation(myRateLimiter)")
    public void pointcut(MyRateLimiter myRateLimiter) {
    }

    @Around(value = "pointcut(myRateLimiter)", argNames = "joinPoint,myRateLimiter")
    public Object around(ProceedingJoinPoint joinPoint, MyRateLimiter myRateLimiter) throws Throwable {
        double rate = myRateLimiter.rate();
        int timeout = myRateLimiter.timeout();

        rateLimiterComponent.setRate(rate);

        // 判断客户端获取令牌是否超时
        boolean tryAcquire = rateLimiterComponent.tryAcquire(timeout);
        if (!tryAcquire) {
            // 服务降级
            return rateLimiterComponent.fullback();
        }

        // 获取到令牌，直接执行
        log.info("获取令牌成功，请求执行");
        return joinPoint.proceed();
    }
}
