package com.wangguangwu.standard.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangguangwu
 */
@Slf4j
@Aspect
@Component
public class ExceptionLoggingAspect {

    @Pointcut("execution(public * com.wangguangwu.standard.controller.*.*(..))")
    public void exceptionHandler() {
    }

    @SuppressWarnings("unused")
    @AfterThrowing(pointcut = "exceptionHandler()", throwing = "e")
    public void logErrorRequest(JoinPoint joinPoint, Throwable e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        System.out.println("hello");
        System.out.println("hello2");
        System.out.println("hello3");

        System.out.println("======");
        HttpServletRequest request = attributes.getRequest();
        log.error("error url: {}, message: {}, e: ", request.getRequestURL().toString(), e.getMessage(), e);
    }
}
