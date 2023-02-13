package com.wangguangwu.mybatis.mbg.mapper;

import com.wangguangwu.mybatis.mbg.model.User;
import com.wangguangwu.mybatis.mbg.model.UserExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* Created by Mybatis Generator on 2023/02/13
*/
public interface UserMapper {
    long countByExample(UserExample example);

    int deleteByExample(UserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    List<User> selectByExample(UserExample example);

    User selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") User record, @Param("example") UserExample example);

    int updateByExample(@Param("record") User record, @Param("example") UserExample example);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
}