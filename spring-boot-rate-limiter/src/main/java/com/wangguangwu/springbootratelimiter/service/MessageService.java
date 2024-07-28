package com.wangguangwu.springbootratelimiter.service;

/**
 * 消息服务
 *
 * @author wangguangwu
 */
public interface MessageService {

    /**
     * 发送消息
     *
     * @param message 消息
     * @return 是否成功
     */
    boolean sendMessage(String message);

}
