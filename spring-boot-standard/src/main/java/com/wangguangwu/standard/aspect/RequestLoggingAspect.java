package com.wangguangwu.standard.aspect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wangguangwu.standard.anno.LessLog;
import com.wangguangwu.standard.log.LogOptions;
import com.wangguangwu.standard.log.LogType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Define an aspect to print logging.
 *
 * @author wangguangwu
 */
@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class RequestLoggingAspect {

    final HttpServletRequest httpServletRequest;

    @Around(value = "execution(* com.wangguangwu.standard.controller..*.*(..))")
    public Object aroundBack(final ProceedingJoinPoint joinPoint) throws Throwable {
        LogOptions logOptions = getLogOptions(joinPoint);
        if (logOptions.isUrl()) {
            log.info("request url: {}", httpServletRequest.getRequestURL().toString());
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        // request
        if (logOptions.isRequest()) {
            log.info("request params: {}", gson.toJson(joinPoint.getArgs()));
        }
        Object result = joinPoint.proceed();
        // response
        if (logOptions.isResponse()) {
            log.info("response:{}", gson.toJson(result));
        }
        return result;
    }

    private LogOptions getLogOptions(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        LessLog lessLog = method.getAnnotation(LessLog.class);
        // method is not marked
        if (lessLog == null) {
            return LogOptions.all();
        }
        LogType[] typeArray = lessLog.type();
        EnumSet<LogType> logTypes = EnumSet.copyOf(Arrays.asList(typeArray != null && typeArray.length > 0 ? typeArray : new LogType[]{LogType.NONE}));
        if (logTypes.contains(LogType.NONE)) {
            return LogOptions.all();
        }
        if (logTypes.contains(LogType.ALL)) {
            return LogOptions.none();
        }
        return new LogOptions(!logTypes.contains(LogType.URL), !logTypes.contains(LogType.REQUEST), !logTypes.contains(LogType.RESPONSE));
    }
}
