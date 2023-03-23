package com.wangguangwu.mybatisplus.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.wangguangwu.mybatisplus.entity.UserDO;
import com.wangguangwu.mybatisplus.mapper.UserMapper;
import com.wangguangwu.mybatisplus.service.UserDynamicService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wangguangwu
 */
@Service
@AllArgsConstructor
public class UserDynamicServiceImpl implements UserDynamicService {

    private final UserMapper userMapper;

    @Override
    public List<UserDO> selectAllDefault() {
        return userMapper.selectList(null);
    }

    @DS("slave_1")
    @Override
    public List<UserDO> selectAllSlave() {
        return userMapper.selectList(null);
    }
}
