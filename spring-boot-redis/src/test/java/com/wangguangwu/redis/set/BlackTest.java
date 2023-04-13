package com.wangguangwu.redis.set;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
@DisplayName("使用 Set 结构模拟黑名单")
public class BlackTest {

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
    @DisplayName("模拟黑名单")
    void testBlackSet() throws Exception {
        long userId = 1;
        new Thread(() -> {
            try {
                Thread.sleep((1 + ThreadLocalRandom.current().nextInt(10)) * 1000);
                asyncCommands.sadd("blackUserIds", String.valueOf(userId))
                        .get(1, TimeUnit.SECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        for (int productId = 0; productId < 10; productId++) {
            // 检查 userId 是不是在黑名单里面
            try {
                boolean result = addProduct(userId, productId);
            } catch (Exception exception) {
                faceCheck(userId);
            }
            // 购物中
            TimeUnit.SECONDS.sleep(1);
        }
    }

    public boolean addProduct(long userId, long productId) throws Exception {
        Boolean black = asyncCommands.sismember("blackUserIds", String.valueOf(userId))
                .get(1, TimeUnit.SECONDS);
        if (black) {
            System.out.println("帐号存在风险，请先去完成人脸验证...");
            throw new RuntimeException("risk user");
        }
        Boolean result = asyncCommands.hset("cart_" + userId,
                String.valueOf(productId), "1").get(1, TimeUnit.SECONDS);
        if (result) {
            System.out.println("添加购物车成功,productId:" + productId);
            return true;
        }
        return false;
    }

    public void faceCheck(long userId) throws Exception {
        System.out.println("人脸验证中...");
        TimeUnit.SECONDS.sleep(3);
        System.out.println("人脸验证完成...");
        asyncCommands
                .srem("blackUserIds", String.valueOf(userId)).get(1, TimeUnit.SECONDS);
    }
}
