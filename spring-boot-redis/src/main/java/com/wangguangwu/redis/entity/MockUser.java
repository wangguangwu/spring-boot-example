package com.wangguangwu.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangguangwu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MockUser {

    private Long id;

    private String name;

    private Integer age;

    private String mobile;

    private String password;

    private String headImage;

}
