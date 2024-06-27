package com.wangguangwu.rocketmq.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * @author wangguangwu
 */
public class Consumer {

    public static void main(String[] args) {
        try {
            // 创建消息消费者
            DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-group");
            // 指定 NameServer 地址
            consumer.setNamesrvAddr("121.4.119.252:9876");
            // 指定消费者订阅的主题和标签
            consumer.subscribe("topic", "*");
            // 设置回调函数，编写处理消息的方法
            consumer.registerMessageListener(
                    (MessageListenerConcurrently) (list, consumeConcurrentlyContext) -> {
                        for (MessageExt messageExt : list) {
                            System.out.println("获取消息：" + new String(messageExt.getBody()));
                        }
                        // 返回消费状态
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    });
            // 启动消费者
            consumer.start();
            System.out.println("Consumer Started");
        } catch (MQClientException e) {
            e.printStackTrace();
        }
    }

}
