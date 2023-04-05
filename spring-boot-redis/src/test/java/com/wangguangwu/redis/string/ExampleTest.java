package com.wangguangwu.redis.string;

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
@DisplayName("使用示例")
public class ExampleTest {

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
    void testExample() throws ExecutionException, InterruptedException, TimeoutException {
        String set = asyncCommands.set("name", "wang").get(1, TimeUnit.SECONDS);
        Assertions.assertEquals("OK", set);
        String get = asyncCommands.get("name").get(1, TimeUnit.SECONDS);
        Assertions.assertEquals("wang", get);
    }
}
