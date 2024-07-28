package com.wangguangwu.springbootresilience4j.service.impl;

import com.wangguangwu.springbootresilience4j.service.BulkheadService;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import org.springframework.stereotype.Service;

/**
 * 舱壁模式。
 * <p>
 * 限制并发执行的线程数量，防止某个服务耗尽所有资源。
 *
 * @author wangguangwu
 */
@Service
public class BulkheadServiceImpl implements BulkheadService {

    @Override
    @Bulkhead(name = "backendE")
    public String doSomething() {
        // 受舱壁模式保护的方法逻辑
        return "Success!";
    }
}

