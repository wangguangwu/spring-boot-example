package com.wangguangwu.mybatisplus.service.impl;

import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import com.wangguangwu.mybatisplus.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangguangwu
 * @since 2023-03-06
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
