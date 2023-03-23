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

}
