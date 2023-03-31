package com.wangguangwu.mybatisplus.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
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

    @Override
    @DS("slave_1")
    public List<UserDO> selectAllSlave() {
        return userMapper.selectList(null);
    }

    @Override
    @DS("slave_1")
    public boolean insert(UserDO user) {
        return userMapper.insert(user) == 1;
    }

    @Override
    @DSTransactional
    public void insertWithTransaction(UserDO user) {
        boolean result = insert(user);
        // 在这里抛出异常，以便事务回滚
        throw new MybatisPlusException(result + ":An error occurred during the transaction");
    }
}
