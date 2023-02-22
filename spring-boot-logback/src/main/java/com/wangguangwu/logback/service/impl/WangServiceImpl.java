package com.wangguangwu.logback.service.impl;

import com.wangguangwu.logback.service.WangService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangguangwu
 */
@Slf4j
@Service
public class WangServiceImpl implements WangService {

    @Override
    public void hello() {
        log.debug("service--debug");
        log.info("service--info");
        log.warn("service--warn");
        log.error("service--error");
    }
}
