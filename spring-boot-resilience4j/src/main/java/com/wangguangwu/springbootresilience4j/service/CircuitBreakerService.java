package com.wangguangwu.springbootresilience4j.service;

/**
 * 熔断器
 *
 * @author wangguangwu
 */
public interface CircuitBreakerService {

    /**
     * doSomething
     *
     * @return result
     */
    String doSomething();

}
