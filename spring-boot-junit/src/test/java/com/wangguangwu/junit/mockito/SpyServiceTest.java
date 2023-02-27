package com.wangguangwu.junit.mockito;

import com.wangguangwu.junit.entity.UserDO;
import com.wangguangwu.junit.mapper.UserMapper;
import com.wangguangwu.junit.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

/**
 * @author wangguangwu
 */
@ExtendWith(MockitoExtension.class)
class SpyServiceTest {

    @Spy
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testSaveUser() {
        doReturn(1).when(userMapper).insert(any(UserDO.class));
        doReturn(new UserDO(1L, "Tom")).when(userMapper).selectById(1L);
        UserDO user = new UserDO(1L, "Tom");
        userService.save(user);
        UserDO savedUser = userService.getById(1L);
        Assertions.assertEquals(user.getId(), savedUser.getId());
        Assertions.assertEquals(user.getUserName(), savedUser.getUserName());
    }
}
