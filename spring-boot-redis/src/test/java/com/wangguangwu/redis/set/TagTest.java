package com.wangguangwu.redis.set;

import com.google.common.collect.ImmutableMap;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wangguangwu
 */
@DisplayName("使用 SET 结构做一个标签系统")
public class TagTest {

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
    void testProductTag() throws Exception {
        Map<Long, String> productDB = ImmutableMap.of(
                1L, "XX连衣裙",
                2L, "XX长裙",
                3L, "XX半身裙"
        );
        // tag_p 前缀加上商品 ID 作为 key
        asyncCommands.sadd("tag_p_1", "短款", "A字裙", "夏款")
                .get(1, TimeUnit.SECONDS);
        asyncCommands.sadd("tag_p_2", "春款", "长袖").get(1, TimeUnit.SECONDS);
        asyncCommands.sadd("tag_p_3", "过膝款", "纯棉", "折扣")
                .get(1, TimeUnit.SECONDS);

        long userId = 1;
        // tag_u 前缀加用户作为 key
        asyncCommands.sadd("tag_u_" + userId, "夏款", "折扣")
                .get(1, TimeUnit.SECONDS);

        for (Long productId : productDB.keySet()) {
            Set<String> result =
                    asyncCommands.sinter("tag_u_" + userId,
                            "tag_p_" + productId).get(1, TimeUnit.SECONDS);
            if (CollectionUtils.isNotEmpty(result)) {
                System.out.println("精选页推荐:" + productDB.get(productId)
                        + ",推荐原因:" + String.join(",", result));
            }
        }
    }
}
