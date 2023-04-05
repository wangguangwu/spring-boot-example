package com.wangguangwu.redis.string;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.*;

/**
 * @author wangguangwu
 */
@DisplayName("使用 String 数据结构来做消息队列")
public class MessageQueueTest {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisAsyncCommands<String, String> asyncCommands;

    @BeforeAll
    public static void init() {
        redisClient = RedisClient.create("redis://127.0.0.1:6379/0");
        connection = redisClient.connect();
        asyncCommands = connection.async();
    }

    @AfterAll
    public static void after() {
        connection.close();
        redisClient.shutdown();
    }

    @Test
    void testMessageQueue() throws InterruptedException {
        String queueKey = "message_queue";
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        int size = 10;

        // Producer
        Runnable producer = () -> {
            for (int i = 0; i < size; i++) {
                String message = "hello_" + i;
                try {
                    // 从队列的左边放入数据
                    asyncCommands.lpush(queueKey, message).get(1, TimeUnit.SECONDS);
                    System.out.println("Produced: " + message);
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };

        // Consumer
        Runnable consumer = () -> {
            for (int i = 0; i < size; i++) {
                try {
                    // 从队列的右边拿出数据
                    String message = asyncCommands.rpop(queueKey).get(1, TimeUnit.SECONDS);
                    if (message != null) {
                        System.out.println("Consumed: " + message);
                    }
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        };

        executorService.submit(producer);
        executorService.submit(consumer);

        // executorService.shutdown() 方法会发出关闭信号，通知线程池不再接受新任务，同时允许已经提交的任务执行完毕。
        // 然而，这个方法并不会阻塞调用线程，所以在调用 shutdown() 之后，主线程会继续执行。
        executorService.shutdown();
        // executorService.awaitTermination() 方法，这个方法会阻塞调用线程，直到线程池中的所有任务执行完毕、指定的等待时间到达或者线程被中断。
        // 在这个例子中，我们设置了最长等待时间为 5 秒，如果线程池在 5 秒内完成所有任务，那么主线程会继续执行；
        // 如果 5 秒后仍有未完成的任务，那么 awaitTermination() 方法返回，主线程也会继续执行。
        executorService.awaitTermination(5, TimeUnit.SECONDS);
    }
}
