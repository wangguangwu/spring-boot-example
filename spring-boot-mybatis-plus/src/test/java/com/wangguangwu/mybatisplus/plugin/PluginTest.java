package com.wangguangwu.mybatisplus.plugin;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 乐观锁可能导致丢失更新问题
 *
 * @author wangguangwu
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PluginTest {

    @Resource
    private UserMapper userMapper;

    @Test
    @Order(1)
    @DisplayName("测试分页插件")
    void testPagePlugin() {
        Page<UserDO> page = new Page<>(1, 10);
        Page<UserDO> result = userMapper.selectPage(page, new QueryWrapper<>());
        Assertions.assertAll(
                () -> Assertions.assertEquals(1, result.getCurrent()),
                () -> Assertions.assertEquals(2, result.getPages()),
                () -> Assertions.assertEquals(10, result.getRecords().size()),
                () -> Assertions.assertEquals(20, result.getTotal())
        );
    }

    @Test
    @Order(2)
    @DisplayName("测试乐观锁插件")
    @Transactional
    void testOptimisticLocking() {
        // 1. 查询用户
        Long userId = 1L;
        UserDO user = userMapper.selectById(userId);
        Assertions.assertNotNull(user, "User not found");
        // 当执行 updateById 方法后，MyBatis-Plus 会自动更新内存中的 user 对象的版本字段，所以先取出对应的值
        Integer originalVersion = user.getVersion();

        // 2. 修改用户信息
        user.setUserName("newUserName");
        int updateCount = userMapper.updateById(user);
        Assertions.assertEquals(1, updateCount, "Update should be successful");

        // 3. 再次查询用户
        UserDO updatedUser = userMapper.selectById(userId);
        Assertions.assertNotNull(updatedUser, "Updated user not found");

        // 4. 检查乐观锁字段是否已更新
        Assertions.assertEquals(originalVersion + 1, updatedUser.getVersion(), "Version should be incremented");

        // 5. 尝试使用过期的乐观锁值进行更新
        user.setUserName("oldUserName");
        user.setVersion(originalVersion);
        updateCount = userMapper.updateById(user);
        Assertions.assertEquals(0, updateCount, "Update should fail due to outdated version");

        // 6. 确保用户信息没有被更新
        UserDO notUpdatedUser = userMapper.selectById(userId);
        Assertions.assertEquals("newUserName", notUpdatedUser.getUserName(), "User name should not be updated");
    }
}

