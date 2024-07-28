package com.wangguangwu.springbootresilience4j.service;

import java.util.concurrent.CompletableFuture;

/**
 * 限流器。
 *
 * @author wangguangwu
 */
public interface TimeLimiterService {

    /**
     * doSomething
     *
     * @return result
     */
    CompletableFuture<String> doSomething();

}
