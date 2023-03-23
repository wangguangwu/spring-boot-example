package com.wangguangwu.mybatisplus.service;

import com.wangguangwu.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDynamicServiceTest {

    @Resource
    private UserDynamicService userDynamicService;

    @Test
    @Order(1)
    void testSelectAllDefault() {
        List<UserDO> list = userDynamicService.selectAllDefault();
        Assertions.assertEquals(20, list.size());
        list.forEach(System.out::println);
    }

    @Test
    @Order(2)
    void testSelectAllSlave() {
        List<UserDO> list = userDynamicService.selectAllSlave();
        Assertions.assertEquals(1, list.size());
        list.forEach(System.out::println);
    }
}
