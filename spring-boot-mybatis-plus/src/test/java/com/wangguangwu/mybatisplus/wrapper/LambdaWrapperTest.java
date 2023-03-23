package com.wangguangwu.mybatisplus.wrapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LambdaWrapperTest {

    @Resource
    private UserMapper userMapper;

    @Test
    @Order(1)
    @DisplayName("Lambda 方式实现查询")
    void testLambdaSelect() {
        LambdaQueryWrapper<UserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 使用 Lambda 表达式作为查询条件
        lambdaQueryWrapper.eq(UserDO::getUserName, "张三")
                .ge(UserDO::getUserAge, 24);
        List<UserDO> list = userMapper.selectList(lambdaQueryWrapper);
        Assertions.assertEquals(1, list.size());
        list.forEach(System.out::println);
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    @DisplayName("Lambda 方式实现更新")
    void testLambdaUpdate() {
        LambdaUpdateWrapper<UserDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        // 使用 Lambda 表达式作为更新条件和更新值
        lambdaUpdateWrapper.eq(UserDO::getUserName, "张三")
                .ge(UserDO::getUserAge, 24)
                .set(UserDO::getUserAge, 26);
        int rows = userMapper.update(null, lambdaUpdateWrapper);
        Assertions.assertEquals(1, rows);
    }

    @Test
    @Transactional
    @Rollback
    @Order(3)
    @DisplayName("Lambda 方式实现删除")
    void testLambdaDelete() {
        LambdaQueryWrapper<UserDO> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 使用 Lambda 表达式作为删除条件
        lambdaQueryWrapper.eq(UserDO::getUserName, "张三")
                .ge(UserDO::getUserAge, 24);
        int rows = userMapper.delete(lambdaQueryWrapper);
        Assertions.assertEquals(1, rows);
    }
}
