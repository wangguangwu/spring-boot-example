package com.wangguangwu.responseandexception.exception;

import com.wangguangwu.responseandexception.enums.ResponseEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wangguangwu
 */
@SuppressWarnings("unused")
@EqualsAndHashCode(callSuper = true)
@Data
public class ServiceException extends RuntimeException {

    protected final Integer data;
    protected final String message;

    public ServiceException() {
        this.data = ResponseEnum.SERVICE_UNKNOWN.getCode();
        this.message = ResponseEnum.SERVICE_UNKNOWN.getMessage();
    }

    public ServiceException(String message) {
        this.data = ResponseEnum.SERVICE_UNKNOWN.getCode();
        this.message = message;
    }
}
