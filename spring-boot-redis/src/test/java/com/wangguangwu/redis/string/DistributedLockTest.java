package com.wangguangwu.redis.string;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.concurrent.*;

/**
 * @author wangguangwu
 */
@DisplayName("使用 String 数据结构来做分布式锁")
public class DistributedLockTest {

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
    @Disabled
    @DisplayName("模拟多线程场景下设置分布式锁")
    void testLock() throws Exception {
        long start = System.currentTimeMillis();
        int threadNum = 3;
        CountDownLatch countDownLatch = new CountDownLatch(threadNum);
        ExecutorService executorService = Executors.newFixedThreadPool(threadNum);
        ScheduledExecutorService lockExtender = Executors.newSingleThreadScheduledExecutor();

        // 创建任务
        Runnable runnable = () -> {
            try {
                String threadName = Thread.currentThread().getName();
                while (true) {
                    // 不存在则创建，并且过期时间设置为 5 s
                    SetArgs setArgs = SetArgs.Builder.ex(5).nx();
                    String result = asyncCommands.set("distribute-lock", threadName, setArgs)
                            .get(1, TimeUnit.SECONDS);
                    if ("OK".equals(result)) {
                        // 加锁成功
                        System.out.println(threadName + "加锁成功");
                        break;
                    } else {
                        // 加锁失败
                        System.out.println(threadName + "加锁失败，自旋转等待锁");
                        TimeUnit.MILLISECONDS.sleep(100);
                    }
                }
                // 加锁成功，使用 ScheduledExecutorService 定期为锁续期
                // 此种续期方法并不是完全安全，只作为演示，在高并发场景下会出现竞争条件。
                // 实际生产种，还是使用 Redisson
                ScheduledFuture<?> extendLockFuture = lockExtender.scheduleAtFixedRate(() -> {
                    asyncCommands.expire("distribute-lock", 5); // 续期 5 秒
                    System.out.println(threadName + "锁续期");
                }, 3, 3, TimeUnit.SECONDS);

                System.out.println(threadName + "开始执行业务逻辑");
                TimeUnit.SECONDS.sleep(8);
                System.out.println(threadName + "完成业务逻辑");
                // 任务完成，取消锁续期任务并释放锁
                extendLockFuture.cancel(true);
                asyncCommands.del("distribute-lock").get(1, TimeUnit.SECONDS);
                System.out.println(threadName + "释放锁");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        };
        // 提交任务
        for (int i = 0; i < threadNum; i++) {
            executorService.submit(runnable);
        }

        // 等待所有任务完成
        countDownLatch.await();
        executorService.shutdown();
        Assertions.assertTrue((System.currentTimeMillis() - start) / 1000 - 24 >= 0);
    }
}
