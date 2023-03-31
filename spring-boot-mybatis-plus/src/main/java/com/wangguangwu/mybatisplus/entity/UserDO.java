package com.wangguangwu.mybatisplus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author wangguangwu
 * @since 2023-03-06
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("USER")
public class UserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @TableField("user_name")
    private String userName;

    /**
     * 用户年纪
     */
    @TableField("user_age")
    private Integer userAge;

    /**
     * 用户地址
     */
    @TableField("user_address")
    private String userAddress;

    /**
     * 乐观锁版本号, 默认从 1 开始
     */
    @TableField("version")
    @Version
    private Integer version;

    /**
     * 是否删除；0：未删除，1：删除
     */
    @TableField("is_deleted")
    @TableLogic
    private Byte isDeleted;

    /**
     * 创建时间
     */
    @TableField(value = "gmt_create", fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified", fill = FieldFill.UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime gmtModified;

}
