package com.wangguangwu.standard.response;

import com.wangguangwu.standard.enums.ResponseEnum;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

/**
 * Response type
 *
 * @author wangguangwu
 */
@SuppressWarnings("unused")
@Value
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class Response<T> {

    int code;

    String message;

    T data;

    /**
     * success
     */
    public static <T> Response<T> success(T data) {
        return new Response<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }

    /**
     * error
     */
    public static <T> Response<T> error(int code, String message) {
        return new Response<>(code, message, null);
    }

    /**
     * error
     */
    public static <T> Response<T> error(ResponseEnum resultCodeEnum, T data) {
        return new Response<>(resultCodeEnum.getCode(), resultCodeEnum.getMessage(), data);
    }
}
