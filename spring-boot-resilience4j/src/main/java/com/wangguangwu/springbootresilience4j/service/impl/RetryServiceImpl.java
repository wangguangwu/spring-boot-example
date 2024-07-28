package com.wangguangwu.springbootresilience4j.service.impl;

import com.wangguangwu.springbootresilience4j.service.RetryService;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

/**
 * 重试机制。
 * <p>
 * 在方法调用失败时进行自动重试。
 *
 * @author wangguangwu
 */
@Service
public class RetryServiceImpl implements RetryService {

    @Override
    @Retry(name = "backendC")
    public String doSomething() {
        // 可能会抛出异常的逻辑
        return "Success!";
    }
}
