package com.wangguangwu.xxljob.aop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.xxl.job.core.context.XxlJobHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * @author wangguangwu
 */
@Aspect
@Component
public class ParamHandlerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ParamHandlerAspect.class);

    @Pointcut("@annotation(com.wangguangwu.xxljob.anno.XxlJobJsonParam)")
    public void xxlJobAspect() {
    }

    @Around("xxlJobAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取方法参数类型数组
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?>[] parameterTypes = signature.getParameterTypes();
        String json = XxlJobHelper.getJobParam();
        Object o = null;
        try {
            o = JSON.parseObject(json, parameterTypes[0]);
        } catch (JSONException e) {
            // ignore
        }
        Object[] args = new Object[]{o};
        // 调用方法
        return joinPoint.proceed(args);
    }

    /**
     * 解析参数
     *
     * @param parameterTypes 方法参数类型
     * @param json           JSON 字符串
     * @return 方法参数值
     */
    private Object[] parseArgs(Class<?>[] parameterTypes, String json) {
        Object[] args = new Object[parameterTypes.length];
        try {
            Class[] aClass = JSON.parseObject(json, parameterTypes.getClass());
            args[0] = aClass;
        } catch (JSONException e) {
            logger.error("JSON 解析出错", e);
        }
        return args;
    }
}
