package com.wangguangwu.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wangguangwu
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private String name;

    private double price;

    private String desc;

}
