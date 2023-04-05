package com.wangguangwu.redis.string;

import com.alibaba.fastjson.JSON;
import com.wangguangwu.redis.entity.Product;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 使用 String 数据结构来做缓存
 *
 * @author wangguangwu
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("使用 String 数据结构来做缓存")
class CacheTest {

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
    @DisplayName("存入数据")
    void testCacheProduct() throws ExecutionException, InterruptedException, TimeoutException {
        Product product = Product.builder().name("杯子").price(10d).desc("这是一个杯子").build();
        String json = JSON.toJSONString(product);

        String result = asyncCommands.set("product", json).get(1, TimeUnit.SECONDS);
        Assertions.assertEquals("OK", result);
    }

    @Test
    @Order(2)
    @DisplayName("取出数据")
    void testGetProduct() throws ExecutionException, InterruptedException, TimeoutException {
        String json = asyncCommands.get("product").get(1, TimeUnit.SECONDS);
        Assertions.assertNotNull(json);
        Product product = JSON.parseObject(json, Product.class);
        System.out.println(product);
    }
}
