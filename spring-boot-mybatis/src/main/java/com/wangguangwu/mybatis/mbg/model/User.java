package com.wangguangwu.mybatis.mbg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 *
 * @author wangguangwu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 主键
     *
     * @mbg.generated
     */
    private Long id;

    /**
     * 用户名
     *
     * @mbg.generated
     */
    private String userName;

    /**
     * 用户年纪
     *
     * @mbg.generated
     */
    private Byte userAge;

    /**
     * 用户地址
     *
     * @mbg.generated
     */
    private String userAddress;

    /**
     * 是否删除；0：未删除，1：删除
     *
     * @mbg.generated
     */
    private Byte isDeleted;

    /**
     * 创建时间
     *
     * @mbg.generated
     */
    private Date gmtCreate;

    /**
     * 更新时间
     *
     * @mbg.generated
     */
    private Date gmtModified;
}