package com.wangguangwu.mybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
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
class WrapperBakTest {

    @Resource
    private UserMapper userMapper;

    QueryWrapper<UserDO> queryWrapper;

    LambdaQueryWrapper<UserDO> lambdaQueryWrapper;

    LambdaQueryChainWrapper<UserDO> lambdaQueryChainWrapper;

    UserDO user = new UserDO();

    @BeforeEach
    void init() {
        user = UserDO.builder().userName("张三").userAge(24).build();

        // QueryWrapper
        // 需要硬编码注入
        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", "张三");
        queryWrapper.ge("user_age", 20);


        // lambdaQueryWrapper
        // 可以使用 类::方法 方式调用，并且可以做条件判断
        lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(true, UserDO::getUserName, "张三").ge(UserDO::getUserAge, 20);

        // lambdaQueryChainWrapper
        // 不需要注入实体
        // 可以直接调用 list()、count() 方法，不需要手动执行查询
        lambdaQueryChainWrapper = new LambdaQueryChainWrapper<>(userMapper);
        lambdaQueryChainWrapper.eq(true, UserDO::getUserName, "张三").ge(UserDO::getUserAge, 20);
    }

    @Test
    @Transactional
    @Rollback
    @Order(1)
    void testSelectList() {
        // QueryWrapper
        List<UserDO> listQueryWrapper = userMapper.selectList(queryWrapper);
        queryWrapper.select("user_name", "user_age");
        Assertions.assertEquals(1, listQueryWrapper.size());

        // lambdaQueryWrapper
        lambdaQueryWrapper.select(UserDO::getUserName, UserDO::getUserAge);
        List<UserDO> listLambdaQueryWrapper = userMapper.selectList(lambdaQueryWrapper);
        Assertions.assertEquals(1, listLambdaQueryWrapper.size());

        // lambdaQueryChainWrapper
        lambdaQueryChainWrapper.select(UserDO::getUserName, UserDO::getUserAge);
        List<UserDO> listLambdaQueryChainWrapper = lambdaQueryChainWrapper.list();
        Assertions.assertEquals(1, listLambdaQueryChainWrapper.size());
    }

    @Test
    @Transactional
    @Rollback
    @Order(2)
    void testSelectOne() {
        int times = 3;
        for (int i = 0; i < times; i++) {
            userMapper.insert(user);
        }

        // QueryWrapper
        // 如果查询结果数目大于 1，会抛出 TooManyResultsException 异常
        queryWrapper.last("LIMIT 1");
        UserDO oneQueryWrapper = userMapper.selectOne(queryWrapper);
        Assertions.assertNotNull(oneQueryWrapper);

        // lambdaQueryWrapper
        // 如果查询结果数目大于 1，会抛出 TooManyResultsException 异常
        lambdaQueryWrapper.last("LIMIT 1");
        UserDO oneLambdaQueryWrapper = userMapper.selectOne(lambdaQueryWrapper);
        Assertions.assertNotNull(oneLambdaQueryWrapper);

        // lambdaQueryChainWrapper
        lambdaQueryChainWrapper.last("LIMIT 1");
        UserDO oneLambdaQueryChainWrapper = lambdaQueryChainWrapper.one();
        Assertions.assertNotNull(oneLambdaQueryChainWrapper);
    }

    @Test
    @Transactional
    @Rollback
    @Order(3)
    void testSelectCount() {
        // QueryWrapper
        // 如果查询结果数目大于 1，会抛出 TooManyResultsException 异常
        Long countQueryWrapper = userMapper.selectCount(queryWrapper);
        Assertions.assertEquals(1, countQueryWrapper);

        // lambdaQueryWrapper
        // 如果查询结果数目大于 1，会抛出 TooManyResultsException 异常
        Long countLambdaQueryWrapper = userMapper.selectCount(lambdaQueryWrapper);
        Assertions.assertEquals(1, countLambdaQueryWrapper);

        // lambdaQueryChainWrapper
        Long countLambdaQueryChainWrapper = lambdaQueryChainWrapper.count();
        Assertions.assertNotNull(countLambdaQueryChainWrapper);
    }

    @Test
    @Transactional
    @Rollback
    @Order(4)
    void testUpdate() {
        // UpdateWrapper
        UpdateWrapper<UserDO> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("user_name", "张三").set("user_address", "北京市");
        int updateUpdateWrapper = userMapper.update(user, updateWrapper);
        Assertions.assertEquals(1, updateUpdateWrapper);

        // LambdaUpdateWrapper
        LambdaUpdateWrapper<UserDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(UserDO::getUserName, "张三").set(UserDO::getUserAddress, "芜湖市");
        int updateLambdaUpdateWrapper = userMapper.update(user, lambdaUpdateWrapper);
        Assertions.assertEquals(1, updateLambdaUpdateWrapper);

        // LambdaChainUpdateWrapper
        LambdaUpdateChainWrapper<UserDO> lambdaUpdateChainWrapper = new LambdaUpdateChainWrapper<>(userMapper);
        lambdaUpdateChainWrapper.eq(UserDO::getUserName, "张三").set(UserDO::getUserAddress, "芜湖市");
        boolean updateLambdaUpdateChainWrapper = lambdaUpdateChainWrapper.update(user);
        Assertions.assertTrue(updateLambdaUpdateChainWrapper);
    }

    @Test
    void testDelete() {
        userMapper.delete(queryWrapper);
        userMapper.delete(lambdaQueryWrapper);
        userMapper.delete(lambdaQueryChainWrapper);
    }
}
