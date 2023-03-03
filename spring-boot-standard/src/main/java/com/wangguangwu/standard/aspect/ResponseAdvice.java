package com.wangguangwu.standard.aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wangguangwu.standard.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.json.JsonParseException;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * Define a response advice
 * that intercepts the response body and adds a success status code
 * if the original body is null or wraps it with a Result object if it doesn't already have one.
 *
 * @author wangguangwu
 */
@RequiredArgsConstructor
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body == null) {
            return Response.success(null);
        }
        // body already meet the requirements
        if (body instanceof Response) {
            return body;
        }
        if (body instanceof String) {
            try {
                return objectMapper.writeValueAsString(Response.success(body));
            } catch (JsonProcessingException e) {
                throw new JsonParseException(e);
            }
        }
        return Response.success(body);
    }
}
