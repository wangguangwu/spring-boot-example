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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("使用 String 数据结构来做计数器")
public class CounterTest {

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
    @Order(1)
    @DisplayName("初始化计数器")
    void testInitCounter() throws ExecutionException, InterruptedException, TimeoutException {
        String result = asyncCommands.set("counter", "0").get(1, TimeUnit.SECONDS);
        Assertions.assertEquals("OK", result);
    }

    @Test
    @Order(2)
    @DisplayName("递增计数器")
    void testIncrementCounter() throws ExecutionException, InterruptedException, TimeoutException {
        Long value1 = asyncCommands.incr("counter").get(1, TimeUnit.SECONDS);
        Assertions.assertEquals(1, value1);
        Long value2 = asyncCommands.incrby("counter", 3L).get(1, TimeUnit.SECONDS);
        Assertions.assertEquals(4, value2);
    }

    @Test
    @Order(3)
    @DisplayName("递减计数器")
    void testDecrementCounter() throws ExecutionException, InterruptedException, TimeoutException {
        Long value1 = asyncCommands.decr("counter").get(1, TimeUnit.SECONDS);
        Assertions.assertEquals(3, value1);
        Long value2 = asyncCommands.decrby("counter", 3L).get(1, TimeUnit.SECONDS);
        Assertions.assertEquals(0, value2);
    }
}
