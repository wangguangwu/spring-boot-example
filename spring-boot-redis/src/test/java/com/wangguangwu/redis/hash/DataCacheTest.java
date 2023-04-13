package com.wangguangwu.redis.hash;

import com.wangguangwu.redis.entity.MockUser;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author wangguangwu
 */
@DisplayName("使用 Hash 结构实现数据缓存")
public class DataCacheTest {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;
    private static RedisAsyncCommands<String, String> asyncCommands;
    private static Map<String, MockUser> userDB = new HashMap<>();
    private static final String USER_CACHE_PREFIX = "cn_";

    @BeforeAll
    public static void init() {
        RedisURI redisURI = RedisURI.create("redis://127.0.0.1:6379/0");
        redisClient = RedisClient.create(redisURI);
        connection = redisClient.connect();
        asyncCommands = connection.async();
        // 初始化 userDB
        userDB.put("+8613912345678", new MockUser(1L, "zhangsan", 25, "+8613912345678", "123456", "http://xxxx"));
        userDB.put("+8613512345678", new MockUser(2L, "lisi", 25, "+8613512345678", "abcde", "http://xxxx"));
        userDB.put("+8618812345678", new MockUser(3L, "wangwu", 25, "+8618812345678", "654321", "http://xxxx"));
        userDB.put("+8618912345678", new MockUser(4L, "zhaoliu", 25, "+8618912345678", "98765", "http://xxxx"));
    }

    @AfterAll
    public static void after() {
        connection.close();
        redisClient.shutdown();
    }

    @Test
    @DisplayName("测试缓存数据")
    void testUserCache() throws ExecutionException, InterruptedException, InvocationTargetException, TimeoutException, IllegalAccessException, NoSuchMethodException {
        mockLogin("+8613912345678", "654321");
        mockLogin("+8613912345678", "123456");
    }

    private static void mockLogin(String mobile, String password) throws ExecutionException, InterruptedException, TimeoutException, InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        // 根据手机号查询缓存
        String key = USER_CACHE_PREFIX + mobile;
        Map<String, String> userCache = asyncCommands.hgetall(key).get(1, TimeUnit.SECONDS);
        MockUser user;
        if (CollectionUtils.isEmpty(userCache)) {
            System.out.println("缓存 miss，加载 DB");
            user = userDB.get(mobile);
            if (user == null) {
                System.out.println("登录失败");
                return;
            }
            // user 转 map
            Map<String, String> userMap = BeanUtils.describe(user);
            // 写入缓存
            Long result = asyncCommands.hset(key, userMap).get(1, TimeUnit.SECONDS);
            if (result >= 1) {
                System.out.println("UserId:" + user.getId() + "，已进入缓存");
            }
        } else {
            System.out.println("缓存hit");
            user = new MockUser();
            BeanUtils.populate(user, userCache);
        }
        // 校验登陆
        if (password.equals(user.getPassword())) {
            System.out.println(user.getName() + ", 登录成功!");
        } else {
            System.out.println("登录失败");
        }
        System.out.println("================================");
    }
}
