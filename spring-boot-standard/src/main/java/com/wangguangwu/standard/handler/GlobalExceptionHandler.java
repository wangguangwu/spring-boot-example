package com.wangguangwu.standard.handler;

import com.wangguangwu.standard.enums.ResponseEnum;
import com.wangguangwu.standard.exception.ServiceException;
import com.wangguangwu.standard.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Define global exception handling
 *
 * @author wangguangwu
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * handle exception without exceptionHandler
     */
    @ExceptionHandler(Exception.class)
    private Response<String> handleException(Exception exception) {
        return Response.error(ResponseEnum.SYSTEM_UNKNOWN, exception.getMessage());
    }

    /**
     * handle NullPointerException && IndexOutOfBoundsException && ArithmeticException
     */
    @ExceptionHandler({NullPointerException.class, IndexOutOfBoundsException.class, ArithmeticException.class})
    public Response<String> handleRuntimeErrors(RuntimeException exception) {
        if (exception instanceof NullPointerException) {
            return Response.error(ResponseEnum.NULL_POINT, exception.getMessage());
        }
        return Response.error(ResponseEnum.INDEX_OUT_OF_BOUNDS, exception.getMessage());
    }

    /**
     * handle MethodArgumentNotValidException exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    private Response<String> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        return Response.error(ResponseEnum.INVALID_PARAM, exception.getMessage());
    }

    /**
     * handle MethodArgumentTypeMismatchException && MissingServletRequestParameterException && HttpMessageNotReadableException
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class, HttpMessageNotReadableException.class})
    public Response<String> handleRequestParamErrors(Exception exception) {
        return Response.error(ResponseEnum.MISTYPE_PARAM, exception.getMessage());
    }

    /**
     * handle serviceException
     */
    @ExceptionHandler(ServiceException.class)
    private Response<String> handleServiceException(ServiceException exception) {
        return Response.error(ResponseEnum.SERVICE_UNKNOWN.getCode(), exception.getMessage());
    }

    /**
     * handle HttpRequestMethodNotSupportedException
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Response<String> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return Response.error(ResponseEnum.UNSUPPORTED_METHOD, exception.getMessage());
    }

    /**
     * handle DataAccessException
     */
    @ExceptionHandler(DataAccessException.class)
    public Response<String> handleDataAccessException(DataAccessException exception) {
        return Response.error(ResponseEnum.DATABASE_OPERATION_FAILED.getCode(), exception.getMessage());
    }
}
