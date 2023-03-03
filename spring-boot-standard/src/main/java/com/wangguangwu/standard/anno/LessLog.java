package com.wangguangwu.standard.anno;

import com.wangguangwu.standard.log.LogType;

import java.lang.annotation.*;

/**
 *
 * @author wangguangwu
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LessLog {

    /**
     * default none, print all log
     */
    LogType[] type() default LogType.NONE;

}
