package com.wangguangwu.mybatisplus.service;

import com.wangguangwu.mybatisplus.entity.UserDO;

import java.util.List;

/**
 * 使用多数据源
 *
 * @author wangguangwu
 */
public interface UserDynamicService {

    /**
     * 查找默认库
     *
     * @return list of UserDO
     */
    List<UserDO> selectAllDefault();

    /**
     * 查找 slave 库
     *
     * @return list of UserDO
     */
    List<UserDO> selectAllSlave();

    /**
     * 插入数据
     *
     * @param user user
     * @return boolean
     */
    boolean insert(UserDO user);

    /**
     * 在多数据源环境下使用事务
     *
     * @param user user
     */
    void insertWithTransaction(UserDO user);

}
