package com.wangguangwu.mybatisplus.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wangguangwu.mybatisplus.entity.UserDO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * 测试 IService 基础的 CRUD 方法
 *
 * @author wangguangwu
 */
@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class IServiceTest {

    @Mock
    private UserService userService;

    @Test
    @Order(1)
    @DisplayName("测试保存用户")
    void testSave() {
        // 定义测试数据
        UserDO user = UserDO.builder().userName("wang").userAge(24).build();
        // 定义测试行为
        when(userService.save(user)).thenReturn(true).thenReturn(false);
        // 断言验证测试结果
        Assertions.assertTrue(userService.save(user));
        Assertions.assertFalse(userService.save(user));
        // 验证测试行为
        verify(userService, times(2)).save(user);
    }

    @Test
    @Order(2)
    @DisplayName("测试批量保存用户")
    void testSaveBatch() {
        // 定义测试数据
        List<UserDO> list = new ArrayList<>();
        int times = 3;
        for (int i = 0; i < times; i++) {
            UserDO user = UserDO.builder().userName("wang").userAge(i).build();
            list.add(user);
        }
        // 定义测试行为
        when(userService.saveBatch(list)).thenReturn(true).thenReturn(false);
        // 断言验证测试结果
        Assertions.assertTrue(userService.saveBatch(list));
        Assertions.assertFalse(userService.saveBatch(list));
        // 验证测试行为
        verify(userService, times(2)).saveBatch(list);
    }

    @Test
    @Order(3)
    @DisplayName("测试删除用户")
    void testRemove() {
        QueryWrapper<UserDO> wrapper = new QueryWrapper<>();
        wrapper.eq("user_name", "王五");
        when(userService.remove(wrapper)).thenReturn(true).thenThrow(new RuntimeException("error"));
        Assertions.assertTrue(userService.remove(wrapper));
        Assertions.assertThrows(RuntimeException.class, () -> userService.remove(wrapper));
        verify(userService, times(2)).remove(wrapper);
    }

    @Test
    @Order(4)
    @DisplayName("测试根据 id 删除用户")
    void testRemoveById() {
        Long id = 1L;
        when(userService.removeById(id)).thenReturn(true);
        Assertions.assertTrue(userService.removeById(id));
        verify(userService, times(1)).removeById(id);
    }

    @Test
    @Order(5)
    @DisplayName("测试根据 id 更新用户")
    void testUpdateById() {
        UserDO user = UserDO.builder().userName("wang").id(1L).build();
        when(userService.updateById(user)).thenReturn(true);
        boolean updateById = userService.updateById(user);
        Assertions.assertTrue(updateById);
        verify(userService, times(1)).updateById(user);
    }

    @Test
    @Order(6)
    @DisplayName("测试分页")
    void testPage() {
        Page<UserDO> page = new Page<>(2, 10);
        when(userService.page(page)).thenReturn(new Page<>(2, 10, 20, true));
        Page<UserDO> result = userService.page(page);
        Assertions.assertEquals(2, result.getPages());
        verify(userService, times(1)).page(page);
    }

    @Test
    @Order(7)
    @DisplayName("测试查询方法")
    void testList() {
        when(userService.list()).thenReturn(Collections.singletonList(new UserDO()));
        List<UserDO> list = userService.list();
        Assertions.assertEquals(1, list.size());
        verify(userService, times(1)).list();
    }

    @Test
    @Order(8)
    @DisplayName("测试查询单个方法")
    void testSelectOne() {
        when(userService.getOne(null, false)).thenReturn(new UserDO());
        UserDO one = userService.getOne(null, false);
        Assertions.assertNotNull(one);
        verify(userService, times(1)).getOne(null, false);
    }
}
