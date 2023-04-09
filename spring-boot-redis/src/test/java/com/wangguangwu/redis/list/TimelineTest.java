package com.wangguangwu.redis.list;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 List 结构实现时间线功能")
class TimelineTest {

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
    @DisplayName("实现时间线功能")
    void testRedisTimeline() throws ExecutionException, InterruptedException, TimeoutException {
        String timelineKey = "timeLine";

        // 删除存在的数据
        asyncCommands.del(timelineKey).get(1, TimeUnit.SECONDS);

        // 创建日志
        String logEntry1 = createLogEntry("User logged in");
        String logEntry2 = createLogEntry("User updated profile");
        String logEntry3 = createLogEntry("User logged out");

        // 添加日志
        asyncCommands.rpush(timelineKey, logEntry1, logEntry2, logEntry3).get(1, TimeUnit.SECONDS);

        // 取出数据
        List<String> logEntries = asyncCommands.lrange(timelineKey, 0, -1).get(1, TimeUnit.SECONDS);

        // 断言
        Assertions.assertEquals(3, logEntries.size());
        Assertions.assertEquals(logEntry1, logEntries.get(0));
        Assertions.assertEquals(logEntry2, logEntries.get(1));
        Assertions.assertEquals(logEntry3, logEntries.get(2));
    }

    private String createLogEntry(String message) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter) + "-" + message;
    }
}
