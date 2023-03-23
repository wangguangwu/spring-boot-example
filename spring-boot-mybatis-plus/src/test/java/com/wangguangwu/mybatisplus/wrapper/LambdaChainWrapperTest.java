package com.wangguangwu.mybatisplus.wrapper;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
class LambdaChainWrapperTest {
    @Resource
    private UserMapper userMapper;

    LambdaQueryChainWrapper<UserDO> lambdaQueryWrapper;

    LambdaUpdateChainWrapper<UserDO> lambdaUpdateWrapper;

    @BeforeEach
    void init() {
        lambdaQueryWrapper = new LambdaQueryChainWrapper<>(userMapper);
        lambdaUpdateWrapper = new LambdaUpdateChainWrapper<>(userMapper);
    }

    @Test
    @Transactional
    @Rollback
    @Order(1)
    @DisplayName("lambda 方式更新数据")
    void testLambdaUpdate() {
        boolean result = lambdaUpdateWrapper.eq(UserDO::getId, 1)
                .set(UserDO::getUserAge, 30)
                .update();
        Assertions.assertTrue(result);

        UserDO user = lambdaQueryWrapper.eq(UserDO::getId, 1).one();
        Assertions.assertEquals(30, user.getUserAge());
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    @DisplayName("lambda 方式删除数据")
    void testLambdaDelete() {
        boolean result = lambdaUpdateWrapper.eq(UserDO::getId, 1)
                .remove();
        Assertions.assertTrue(result);

        UserDO user = lambdaQueryWrapper.eq(UserDO::getId, 1).one();
        Assertions.assertNull(user);
    }

    @Test
    @Transactional
    @Rollback
    @Order(3)
    @DisplayName("lambda 方式查询数据")
    void testLambdaQuery() {
        List<UserDO> list = lambdaQueryWrapper.select(UserDO::getUserName, UserDO::getUserAge)
                .like(UserDO::getUserName, "张")
                .ge(UserDO::getUserAge, 25)
                .list();
        Assertions.assertEquals(1, list.size());
        list.forEach(System.out::println);
    }

    @Test
    @Transactional
    @Rollback
    @Order(4)
    @DisplayName("lambda 方式分页查询数据")
    void testLambdaQueryPage() {
        Page<UserDO> page = lambdaQueryWrapper.select(UserDO::getId, UserDO::getUserName, UserDO::getUserAge)
                .like(UserDO::getUserName, "张")
                .ge(UserDO::getUserAge, 25)
                .orderByDesc(UserDO::getUserAge)
                .page(new Page<>(1, 10));
        List<UserDO> records = page.getRecords();
        Assertions.assertEquals(1, records.size());
        Assertions.assertEquals(1, page.getTotal());
        records.forEach(System.out::println);
    }
}
