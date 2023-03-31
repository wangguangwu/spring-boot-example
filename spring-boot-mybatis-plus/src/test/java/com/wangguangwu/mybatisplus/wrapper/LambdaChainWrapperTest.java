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

    LambdaQueryChainWrapper<UserDO> lambdaQueryChainWrapper;

    LambdaUpdateChainWrapper<UserDO> lambdaUpdateChainWrapper;

    @BeforeEach
    void init() {
        lambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(userMapper);
        lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(userMapper);
    }

    @Test
    @Transactional
    @Rollback
    @Order(1)
    @DisplayName("lambdaChain 方式更新数据")
    void testLambdaUpdate() {
        boolean result = lambdaUpdateChainWrapper.set(UserDO::getUserAge, 30)
                .eq(UserDO::getId, 1)
                .or()
                .eq(UserDO::getId, 2)
                .update();
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    @DisplayName("lambdaChain 方式删除数据")
    void testLambdaDelete() {
        boolean result = lambdaUpdateChainWrapper.eq(UserDO::getId, 1)
                .or()
                .eq(UserDO::getId, 2)
                .remove();
        Assertions.assertTrue(result);
    }

    @Test
    @Transactional
    @Rollback
    @Order(3)
    @DisplayName("lambdaChain 方式查询数据")
    void testLambdaQuery() {
        List<UserDO> list = lambdaQueryChainWrapper.select(UserDO::getUserName, UserDO::getUserAge)
                .like(UserDO::getUserName, "张")
                .ge(UserDO::getUserAge, 25)
                .or()
                .eq(UserDO::getId, 2)
                .list();
        Assertions.assertEquals(2, list.size());
    }

    @Test
    @Transactional
    @Rollback
    @Order(4)
    @DisplayName("lambdaChain 方式分页查询数据")
    void testLambdaQueryPage() {
        Page<UserDO> page = lambdaQueryChainWrapper.select(UserDO::getId, UserDO::getUserName, UserDO::getUserAge)
                .like(UserDO::getUserName, "张")
                .ge(UserDO::getUserAge, 25)
                .orderByDesc(UserDO::getUserAge)
                .or(wrapper -> wrapper.eq(UserDO::getUserName, "王五").lt(UserDO::getUserAge, 30))
                .page(new Page<>(1, 10));
        List<UserDO> records = page.getRecords();
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals(2, page.getTotal());
    }
}
