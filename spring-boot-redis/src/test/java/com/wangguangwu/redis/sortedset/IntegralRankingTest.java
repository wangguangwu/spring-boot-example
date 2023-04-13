package com.wangguangwu.redis.sortedset;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static jdk.nashorn.internal.objects.NativeNumber.NEGATIVE_INFINITY;
import static jdk.nashorn.internal.objects.NativeNumber.POSITIVE_INFINITY;

/**
 * @author wangguangwu
 */
@DisplayName("使用 ZSorted Set 实现积分排序功能")
public class IntegralRankingTest {

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
    void testSortedSetRank() throws ExecutionException, InterruptedException, TimeoutException {
        List<String> players = Lists.newArrayList(
                "zhangsan", "lisi", "wangwu", "zhaliu", "sunqi"
        );

        // 启动一个线程，模拟加减分操作
        Thread updateThread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    String player = players.get(i);
                    int point = i % 2;
                    System.out.println("对战结果:" + player + "," + point);
                    asyncCommands.zaddincr("NationalRank", point, player)
                            .get(1, TimeUnit.SECONDS);
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException | ExecutionException | TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });
        updateThread.start();

        for (int i = 0; i < 5; i++) {
            List<ScoredValue<String>> nationalRank = asyncCommands.zrevrangebyscoreWithScores("NationalRank",
                            // Range.create(NEGATIVE_INFINITY, POSITIVE_INFINITY)：表示要获取的元素的分值范围
                            // 这里使用了 Range.create 方法创建了一个范围，其下限为负无穷，上限为正无穷，表示获取所有元素。
                            Range.create(NEGATIVE_INFINITY, POSITIVE_INFINITY),
                            // Limit.create(0, 3)：表示返回结果的偏移量和元素数量
                            // 这里使用了 Limit.create 方法创建了一个偏移量为 0，数量为 3 的限制，表示获取分值最高的前三个元素。
                            Limit.create(0, 3))
                    .get(1, TimeUnit.SECONDS);
            System.out.println("NationalRank:");
            for (int j = 0; j < nationalRank.size(); j++) {
                ScoredValue scoredValue = nationalRank.get(j);
                System.out.println("第" + (j + 1) + "名：" + scoredValue.getValue() + ":" + scoredValue.getScore());
            }
            System.out.println("===================");
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
