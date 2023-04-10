package com.wangguangwu.redis.list;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 List 实现提醒功能")
public class RemindTest {

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
    void testListList() throws InterruptedException {
        long videoId = 1089;
        final String listName = "like-list-" + videoId;
        Runnable createLike = () -> {
            for (int i = 0; i < 100000; i++) {
                try {
                    // 模拟点赞
                    asyncCommands.rpush(listName, String.valueOf(i))
                            .get(1, TimeUnit.SECONDS);
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException | TimeoutException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(createLike).start();

        Runnable consumerLike = () -> {
            while (true) {
                try {
                    // 拉取前 1000 个点赞
                    List<String> userIds = asyncCommands.lrange(listName, 0, 1000).get(1, TimeUnit.SECONDS);
                    System.out.println("点赞：" + userIds);
                    TimeUnit.SECONDS.sleep(2);
                    // 删除已经拉取到的点赞信息
                    String s = asyncCommands.ltrim(listName, userIds.size(), -1).get(1, TimeUnit.SECONDS);
                    if ("OK".equals(s)) {
                        System.out.println("删除成功");
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        new Thread(consumerLike).start();
        TimeUnit.HOURS.sleep(1);
    }

}
