package com.wangguangwu.redis.string;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.ScriptOutputType;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author wangguangwu
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("使用 String 数据结构来做限流")
public class RateLimiterTest {

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
    @DisplayName("简单的限流")
    void testLimit() throws ExecutionException, InterruptedException, TimeoutException {
        int count = 0;
        String prefix = "order-service";
        long maxQps = 10;
        long nowSeconds = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 15; i++) {
            // 1s 中最多处理 10 个请求
            Long result = asyncCommands.incr(prefix + nowSeconds).get(1, TimeUnit.SECONDS);
            if (result > maxQps) {
                System.out.println("请求被限流");
            } else {
                System.out.println("请求正常被处理");
                count++;
            }
        }
        Assertions.assertEquals(maxQps, count);
    }

    @Test
    @Order(2)
    @DisplayName("基于 Lua 脚本实现的基于固定窗口的限流算法")
    void testLimitByLua() throws InterruptedException {
        // 最大请求数
        int maxRequests = 5;
        // 时间窗口为 10 s
        int timeWindowInSeconds = 10;
        String key = "rate_limit";
        AtomicInteger passedRequests = new AtomicInteger(0);

        // 模拟请求
        Runnable request = () -> {
            try {
                // 获取当前时间戳（以秒为单位）
                String currentTimestamp = String.valueOf(Instant.now().getEpochSecond());
                // lua 脚本
                String luaScript = "local current = tonumber(redis.call('get', KEYS[1]) or '0')\n" +
                        "local timestamp = tonumber(ARGV[1])\n" +
                        "local max_requests = tonumber(ARGV[2])\n" +
                        "local time_window = tonumber(ARGV[3])\n" +
                        "if current < max_requests then\n" +
                        "  redis.call('incr', KEYS[1])\n" +
                        "  if redis.call('ttl', KEYS[1]) == -1 then\n" +
                        "    redis.call('expire', KEYS[1], time_window)\n" +
                        "  end\n" +
                        "  return 1\n" +
                        "else\n" +
                        "  return 0\n" +
                        "end";
                // 创建一个只包含一个元素（即 Redis 键）的不可变列表，直接转数组也可以，这里是为了方便后面扩展
                List<String> keys = Collections.singletonList(key);
                // 创建一个包含三个元素的列表，分别是当前时间戳、最大请求数（maxRequests）和时间窗口（timeWindowInSeconds）。
                List<String> args = Arrays.asList(currentTimestamp, String.valueOf(maxRequests), String.valueOf(timeWindowInSeconds));
                // 执行 Lua 脚本
                // asyncCommands.eval()：执行 Lua 脚本的方法。
                // luaScript：要执行的 Lua 脚本字符串。
                // ScriptOutputType.INTEGER：脚本执行的结果类型，这里为整数
                // keys.toArray(new String[0])：将键列表转换为字符串数组，作为 eval 方法的第三个参数
                // args.toArray(new String[0])：将参数列表转换为字符串数组，作为 eval 方法的第四个参数
                Long result = (Long) asyncCommands.eval(luaScript, ScriptOutputType.INTEGER, keys.toArray(new String[0]), args.toArray(new String[0])).get(1, TimeUnit.SECONDS);

                if (result == 1) {
                    System.out.println(Thread.currentThread().getName() + " 通过限流");
                    passedRequests.incrementAndGet();
                } else {
                    System.out.println(Thread.currentThread().getName() + " 被限流");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        // 循环 20 次，在每次提交间等待 100 毫秒
        for (int i = 0; i < 20; i++) {
            executorService.submit(request);
            TimeUnit.MILLISECONDS.sleep(100);
        }
        executorService.shutdown();
        executorService.awaitTermination(5, TimeUnit.SECONDS);

        Assertions.assertTrue(passedRequests.get() <= maxRequests, "请求数超过限制");
    }
}
