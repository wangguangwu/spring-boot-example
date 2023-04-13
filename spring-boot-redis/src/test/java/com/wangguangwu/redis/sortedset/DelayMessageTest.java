package com.wangguangwu.redis.sortedset;

import io.lettuce.core.Range;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScoredValue;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 ZSorted Set 结构实现一个延迟队列")
public class DelayMessageTest {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisAsyncCommands<String, String> asyncCommands;

    @BeforeAll
    public static void init() {
        RedisURI redisURI = RedisURI.create("redis://127.0.0.1:6379/0");
        redisClient = RedisClient.create(redisURI);
        connection = redisClient.connect();
        asyncCommands = connection.async();
    }

    @AfterAll
    public static void after() {
        connection.close();
        redisClient.shutdown();
    }

    @Test
    void testDelayMessage() {
        // 启动一个线程用来生成任务
        Thread messageCreator = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    int randDelayTime = ThreadLocalRandom.current().nextInt(10);
                    long current = System.currentTimeMillis() / 1000;
                    long startTime = current + randDelayTime;
                    // 存入任务
                    asyncCommands.zadd("messageCenter", startTime, "task_" + startTime)
                            .get(1, TimeUnit.SECONDS);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
        messageCreator.start();


        // 从 redis 中读取任务并执行
        for (int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(2);
                long current = System.currentTimeMillis() / 1000;
                Long zCount = asyncCommands.zcount("messageCenter", Range.create(0, current)).get(1, TimeUnit.SECONDS);
                if (zCount <= 0) {
                    System.out.println("没有到期消息");
                    continue;
                }
                // 取出任务
                List<ScoredValue<String>> tasks = asyncCommands.zpopmin("messageCenter", zCount).get(1, TimeUnit.SECONDS);
                for (int j = 0; j < tasks.size(); j++) {
                    ScoredValue<String> task = tasks.get(j);
                    System.out.println("发送消息:" + task.getValue() + ", " + Double.valueOf(task.getScore()).longValue());
                }
                System.out.println("==========================");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
