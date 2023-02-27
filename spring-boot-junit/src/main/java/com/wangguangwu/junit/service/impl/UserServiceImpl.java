package com.wangguangwu.junit.service.impl;

import com.wangguangwu.junit.entity.UserDO;
import com.wangguangwu.junit.mapper.UserMapper;
import com.wangguangwu.junit.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author wangguangwu
 * @since 2023-02-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
