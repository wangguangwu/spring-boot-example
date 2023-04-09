package com.wangguangwu.redis.list;

import io.lettuce.core.KeyValue;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author wangguangwu
 */
@DisplayName("使用 List 结构来做消息队列")
public class MqTest {

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
    @DisplayName("生产者")
    void testProducer() throws InterruptedException, ExecutionException, TimeoutException {
        int times = 20;
        for (int i = 0; i < times; i++) {
            TimeUnit.MILLISECONDS.sleep(100);
            String value = String.valueOf(i);
            Long size = asyncCommands.lpush("mq", value)
                    .get(1, TimeUnit.SECONDS);
            System.out.printf("Producer: produce [%s], current MQ length [%s]\n", value, size);
            Assertions.assertTrue(size >= 1);
        }
    }

    @Test
    @Order(2)
    @DisplayName("消费者")
    void testConsumer() {
        while (true) {
            try {
                // 阻塞消费数据
                KeyValue<String, String> data = asyncCommands.brpop(1, "mq").get(2, TimeUnit.SECONDS);
                if (data != null && data.hasValue()) {
                    System.out.printf("Consumer: from [%s] consume data [%s] in the queue\n", data.getKey(), data.getValue());
                } else {
                    System.out.println("Consumer: has not get data, keep listen");
                }
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("多线程场景下使用消费者")
    void testConsumer2() {
        // 使用 Lettuce 的时候，在多线程场景下关于 connection 存在一个问题
        // 使用非阻塞命令时，不同线程间可以共享 connection
        // 使用阻塞命令时，无法共享 connection
        int times = 3;
        ExecutorService executorService = Executors.newFixedThreadPool(times);

        Callable<Void> consumerTask = () -> {
            while (true) {
                try {
                    // 阻塞消费数据
                    // 当多个线程共用一个 connection 去执行阻塞命令，类似于 BRPOP 时，会和 connection 本地的 get() 命令超时时间相互影响
                    KeyValue<String, String> data = asyncCommands.brpop(1, "mq").get(2, TimeUnit.SECONDS);
                    String threadName = Thread.currentThread().getName();
                    if (data != null && data.hasValue()) {
                        System.out.printf("[%s] Consumer: from [%s] consume data [%s] in the queue\n", threadName, data.getKey(), data.getValue());
                    } else {
                        System.out.printf("[%s] Consumer: has not get data, keep listen\n", threadName);
                    }
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    throw e;
                }
            }
        };

        // 只能捕获当前线程（主线程）中的异常
        Assertions.assertThrows(TimeoutException.class, () -> {
            List<Future<Void>> futures = new ArrayList<>();
            for (int i = 0; i < times; i++) {
                // 提交任务，获取执行结果
                futures.add(executorService.submit(consumerTask));
            }
            TimeUnit.SECONDS.sleep(10);
            for (Future<Void> future : futures) {
                try {
                    future.get(10, TimeUnit.SECONDS);
                } catch (InterruptedException | ExecutionException e) {
                    Throwable cause = e.getCause();
                    if (cause instanceof TimeoutException) {
                        throw cause;
                    }
                } catch (TimeoutException e) {
                    // ignore
                }
            }
            // 关闭线程池
            executorService.awaitTermination(3, TimeUnit.SECONDS);
        });
    }
}





