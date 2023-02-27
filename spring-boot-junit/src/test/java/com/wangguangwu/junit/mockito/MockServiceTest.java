package com.wangguangwu.junit.mockito;

import com.wangguangwu.junit.entity.UserDO;
import com.wangguangwu.junit.mapper.UserMapper;
import com.wangguangwu.junit.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;


/**
 * @author wangguangwu
 */
@ExtendWith(MockitoExtension.class)
class MockServiceTest {

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void selectById() {
        when(userMapper.selectById(1L))
                .thenReturn(new UserDO(1L, "hello"));
        UserDO user = userService.getById(1L);
        Assertions.assertEquals("hello", user.getUserName());
    }
}
