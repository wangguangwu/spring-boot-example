package com.wangguangwu.responseandexception.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Response result enum.
 *
 * @author wangguangwu
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ResponseEnum {

    /**
     * SUCCESS
     */
    SUCCESS(0, "成功"),

    /**
     * FAIL
     */
    FAIL(-1, "失败"),

    /**
     * SYSTEM_UNKNOWN
     */
    SYSTEM_UNKNOWN(1000, "其他异常"),

    /**
     * NULL_POINT
     */
    NULL_POINT(1001, "空指针异常"),

    /**
     * INDEX_OUT_OF_BOUNDS
     */
    INDEX_OUT_OF_BOUNDS(1002, "数组越界异常"),

    /**
     * INVALID_PARAM
     */
    INVALID_PARAM(1003, "参数校验失败"),

    /**
     * MISTYPE_PARAM
     */
    MISTYPE_PARAM(1004, "参数格式有误"),

    /**
     * UNSUPPORTED_METHOD
     */
    UNSUPPORTED_METHOD(1005, "不支持的请求格式"),

    /**
     * SERVICE_UNKNOWN
     */
    SERVICE_UNKNOWN(1006, "业务层异常"),

    /**
     * DATABASE_OPERATION_FAILED
     */
    DATABASE_OPERATION_FAILED(1007, "数据库操作失败");

    private final int code;

    private final String message;

}