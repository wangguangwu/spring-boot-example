package com.wangguangwu.springbootratelimiter.service.impl;

import com.wangguangwu.springbootratelimiter.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author wangguangwu
 */
@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Override
    public boolean sendMessage(String message) {
        log.info("消息[{}]发送成功", message);
        return true;
    }
}
