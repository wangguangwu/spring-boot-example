package com.wangguangwu.redis.serialize;

import com.wangguangwu.redis.entity.User;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StopWatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试 Redis 不同序列化方式
 * <p>
 * 如何自定义序列化配置见：{@link com.wangguangwu.redis.config.RedisConfig}
 *
 * @author wangguangwu
 */
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SerializerTest {

    static List<User> list = new ArrayList<>();

    static Map<String, Integer> serializeSizeMap = new HashMap<>();

    static StopWatch serializeStopWatch = new StopWatch();

    static StopWatch deSerializeStopWatch = new StopWatch();

    @BeforeAll
    static void init() {
        for (int i = 0; i < 2000; i++) {
            User user = User.builder().name("wang").age(String.valueOf(i)).build();
            list.add(user);
        }
    }

    @AfterAll
    static void end() {
        System.out.println("========================= serialize used time ==============================");
        System.out.println(serializeStopWatch.prettyPrint());
        System.out.println("========================= deSerialize used time ==============================");
        System.out.println(deSerializeStopWatch.prettyPrint());
        System.out.println("========================= serialize bytes length ==============================");
        for (Map.Entry<String, Integer> entry : serializeSizeMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    @Test
    @Order(1)
    @DisplayName("将任何对象泛化为字符串并序列化")
    void testGenericToStringSerializer() {
        GenericToStringSerializer<Object> serializer = new GenericToStringSerializer<>(Object.class);
        serializeStopWatch.start("[GenericToStringSerializer] serialize");
        byte[] serialize = serializer.serialize(list.toString());
        serializeStopWatch.stop();
        Assertions.assertNotNull(serialize, "serialize failed");
        serializeSizeMap.put("GenericToStringSerializer", serialize.length);
        deSerializeStopWatch.start("[GenericToStringSerializer] deSerialize");
        serializer.deserialize(serialize);
        deSerializeStopWatch.stop();
    }

    @Test
    @Order(2)
    @DisplayName("序列化 object 对象为 json 字符串")
    void testGenericJackson2JsonRedisSerializer() {
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        serializeStopWatch.start("[Jackson2JsonRedisSerializer] serialize");
        byte[] serialize = serializer.serialize(list);
        serializeStopWatch.stop();
        Assertions.assertNotNull(serialize, "serialize failed");
        serializeSizeMap.put("Jackson2JsonRedisSerializer", serialize.length);
        deSerializeStopWatch.start("[Jackson2JsonRedisSerializer] deSerialize");
        serializer.deserialize(serialize);
        deSerializeStopWatch.stop();
    }


    @Test
    @Order(3)
    @DisplayName("序列化 java 对象，实体类需要实现序列化接口")
    void testFastJsonRedisSerializer() {
        JdkSerializationRedisSerializer serializer = new JdkSerializationRedisSerializer();
        serializeStopWatch.start("[JdkSerializationRedisSerializer] serialize");
        byte[] serialize = serializer.serialize(list);
        serializeStopWatch.stop();
        Assertions.assertNotNull(serialize, "serialize failed");
        serializeSizeMap.put("JdkSerializationRedisSerializer", serialize.length);
        deSerializeStopWatch.start("[JdkSerializationRedisSerializer] deSerialize");
        serializer.deserialize(serialize);
        deSerializeStopWatch.stop();
    }

    @Test
    @Order(4)
    @DisplayName("简答的字符串序列化")
    void testStringRedisSerializer() {
        StringRedisSerializer serializer = new StringRedisSerializer();
        serializeStopWatch.start("[StringRedisSerializer] serialize");
        byte[] serialize = serializer.serialize(list.toString());
        serializeStopWatch.stop();
        Assertions.assertNotNull(serialize, "serialize failed");
        serializeSizeMap.put("StringRedisSerializer", serialize.length);
        deSerializeStopWatch.start("[StringRedisSerializer] deSerialize");
        serializer.deserialize(serialize);
        deSerializeStopWatch.stop();
    }
}
