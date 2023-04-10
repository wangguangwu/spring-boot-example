package com.wangguangwu.redis.list;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 list 实现最近访问列表")
public class RecentlyVisitedTest {

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
    @DisplayName("实现最近访问列表")
    void testRecentlyVisited() throws ExecutionException, InterruptedException, TimeoutException {
        String key = "recently-visited";

        // 清空列表
        asyncCommands.del(key).get(1, TimeUnit.SECONDS);

        // 模拟用户访问
        asyncCommands.rpush(key, "1").get(1, TimeUnit.SECONDS);
        asyncCommands.rpush(key, "2").get(1, TimeUnit.SECONDS);
        asyncCommands.rpush(key, "3").get(1, TimeUnit.SECONDS);

        // 取出前两个访问用户
        List<String> list = asyncCommands.lrange(key, 0, 1).get(1, TimeUnit.SECONDS);
        Assertions.assertEquals("1", list.get(0));
        Assertions.assertEquals("2", list.get(1));
    }
}
