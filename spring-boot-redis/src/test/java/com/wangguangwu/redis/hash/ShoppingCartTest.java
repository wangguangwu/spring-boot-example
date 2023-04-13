package com.wangguangwu.redis.hash;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("使用 Hash 结构模拟购物车")
public class ShoppingCartTest {


    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisAsyncCommands<String, String> asyncCommands;
    private static final String CART_PREFIX = "cart_";

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
    void testCardDao() throws Exception {
        add(1024, "83694");
        add(1024, "1273979");
        add(1024, "123323");
        submitOrder(1024);
        remove(1024, "123323");
        submitOrder(1024);

        incr(1024, "83694");
        decr(1024, "1273979");
    }

    //==========================私有方法==================================

    private static void add(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Boolean result = asyncCommands.hset(CART_PREFIX + userId, productId, "1").get(1, TimeUnit.SECONDS);
        if (result) {
            System.out.println("添加购物车成功,productId:" + productId);

        }
    }

    private static void remove(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Long result = asyncCommands.hdel(CART_PREFIX + userId, productId)
                .get(1, TimeUnit.SECONDS);
        if (result == 1) {
            System.out.println("商品删除成功，productId:" + productId);
        }
    }

    private static void submitOrder(long userId) throws ExecutionException, InterruptedException, TimeoutException {
        Map<String, String> cartInfo = asyncCommands.hgetall(CART_PREFIX + userId).get(1, TimeUnit.SECONDS);
        System.out.println("用户:" + userId + ", 提交订单:");
        for (Map.Entry<String, String> entry : cartInfo.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    private static void incr(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        Long result = asyncCommands.hincrby(CART_PREFIX + userId,
                productId, 1).get(1, TimeUnit.SECONDS);
        System.out.println("商品数量加1成功，剩余数量为:" + result);
    }

    private static void decr(long userId, String productId) throws ExecutionException, InterruptedException, TimeoutException {
        String count = asyncCommands.hget(CART_PREFIX + userId,
                productId).get(1, TimeUnit.SECONDS);
        if (Long.parseLong(count) - 1 <= 0) {
            // 删除商品
            remove(userId, productId);
            return;
        }
        Long result = asyncCommands.hincrby(CART_PREFIX + userId,
                productId, -1).get(1, TimeUnit.SECONDS);
        System.out.println("商品数量减1成功，剩余数量为:" + result);
    }
}
