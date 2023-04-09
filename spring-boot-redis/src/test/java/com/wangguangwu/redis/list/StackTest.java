package com.wangguangwu.redis.list;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 List 结构实现堆栈")
public class StackTest {

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
    @DisplayName("实现堆栈")
    void testStack() throws ExecutionException, InterruptedException, TimeoutException {
        String stackKey = "stack";

        // 删除过往数据
        asyncCommands.del(stackKey).get(1, TimeUnit.SECONDS);

        // 往 stack 中存入数据
        asyncCommands.lpush(stackKey, "A").get(1, TimeUnit.SECONDS);
        asyncCommands.lpush(stackKey, "B").get(1, TimeUnit.SECONDS);
        asyncCommands.lpush(stackKey, "C").get(1, TimeUnit.SECONDS);

        // 从 stack 中取出数据
        String firstElement = asyncCommands.lpop(stackKey).get(1, TimeUnit.SECONDS);
        String secondElement = asyncCommands.lpop(stackKey).get(1, TimeUnit.SECONDS);
        String thirdElement = asyncCommands.lpop(stackKey).get(1, TimeUnit.SECONDS);

        // 断言
        Assertions.assertEquals("C", firstElement);
        Assertions.assertEquals("B", secondElement);
        Assertions.assertEquals("A", thirdElement);
    }

}
