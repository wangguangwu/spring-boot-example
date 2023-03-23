package com.wangguangwu.mybatisplus.service;

import com.wangguangwu.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 测试 IService 基础的 CRUD 方法
 *
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest {

    @Resource
    private UserService UserService;

    @Test
    @Transactional
    @Rollback
    @Order(1)
    void testSaveUserDO() {
        UserDO UserDO = new UserDO();
        UserDO.setUserName("张三");
        UserDO.setUserAge(20);
        UserDO.setUserAddress("上海市");
        boolean insert = UserService.save(UserDO);
        Assertions.assertTrue(insert);
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    void testUpdateUserDO() {
        UserDO UserDO = new UserDO();
        UserDO.setId(1L);
        UserDO.setUserAge(30);
        UserDO.setUserAddress("北京市");
        boolean updateById = UserService.updateById(UserDO);
        Assertions.assertTrue(updateById);
    }

    @Test
    @Transactional
    @Rollback
    @Order(3)
    void testGetUserDOById() {
        UserDO UserDO = UserService.getById(1L);
        Assertions.assertNotNull(UserDO);
        Assertions.assertEquals("张三", UserDO.getUserName());
        Assertions.assertEquals(25, UserDO.getUserAge());
        Assertions.assertEquals("北京市朝阳区", UserDO.getUserAddress());
    }

    @Test
    @Transactional
    @Rollback
    @Order(4)
    void testDeleteUserDOById() {
        boolean removeById = UserService.removeById(1L);
        Assertions.assertTrue(removeById);
    }
}
