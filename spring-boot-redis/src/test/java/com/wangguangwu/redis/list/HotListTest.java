package com.wangguangwu.redis.list;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 List 实现热点列表")
public class HotListTest {

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
    void testHotList() throws ExecutionException, InterruptedException, TimeoutException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String key = "hot-list";
        // 删除旧数据
        asyncCommands.del(key).get(1, TimeUnit.SECONDS);
        // 初始化热点列表
        for (int i = 0; i < 10; i++) {
            asyncCommands.rpush(key, "第【" + i + "】条热点新闻，更新时间：" + formatter.format(LocalDateTime.now()));
        }

        // 模拟热点新闻的更新操作
        Runnable updateHotList = () -> {
            while (true) {
                try {
                    int index = ThreadLocalRandom.current().nextInt(0, 10);
                    asyncCommands.lset(key, index, "第【" + index + "】条热点新闻，更新时间：" + formatter.format(LocalDateTime.now()));
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(updateHotList).start();

        // 模拟客户端拉取热点新闻
        for (int i = 0; i < 5; i++) {
            List<String> hosList = asyncCommands.lrange(key, 0, -1).get(1, TimeUnit.SECONDS);
            TimeUnit.SECONDS.sleep(3);
            System.out.println("hot list:");
            hosList.forEach(System.out::println);
            System.out.println("==========================");
        }
    }
}
