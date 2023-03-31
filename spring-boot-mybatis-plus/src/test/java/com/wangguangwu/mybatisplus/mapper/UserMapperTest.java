package com.wangguangwu.mybatisplus.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wangguangwu.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 测试 BaseMapper 的 CRUD 方法
 *
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserMapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    @Transactional
    @Rollback
    @Order(1)
    void testInsert() {
        UserDO user = new UserDO();
        user.setUserName("wangguangwu");
        user.setUserAge(24);
        user.setUserAddress("hangzhou");
        int result = userMapper.insert(user);
        Assertions.assertEquals(1, result);
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    void testUpdateById() {
        UserDO user = new UserDO();
        user.setId(1L);
        user.setUserAge(20);
        int result = userMapper.updateById(user);
        Assertions.assertEquals(1, result);
    }


    @Test
    @Order(3)
    void testSelectById() {
        UserDO user = userMapper.selectById(1L);
        Assertions.assertNotNull(user);
        System.out.println(user);
    }

    @Test
    @Transactional
    @Rollback
    @Order(4)
    void testDeleteById() {
        int result = userMapper.deleteById(1L);
        Assertions.assertEquals(1, result);
    }

    @Test
    @Order(5)
    void testSelectList() {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserDO::getUserName, "张三")
                .select(UserDO::getUserName, UserDO::getUserAge, UserDO::getUserAddress);
        List<UserDO> userList = userMapper.selectList(queryWrapper);
        Assertions.assertEquals(1, userList.size());
    }

    @Test
    @Order(6)
    void testSelectAll() {
        List<UserDO> userList = userMapper.selectList(null);
        Assertions.assertEquals(20, userList.size());
    }
}
