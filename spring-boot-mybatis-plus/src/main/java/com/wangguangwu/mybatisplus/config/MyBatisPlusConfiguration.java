package com.wangguangwu.mybatisplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * 配置插件
 *
 * @author wangguangwu
 */
@Configuration
public class MyBatisPlusConfiguration {

    /**
     * 全局通用字段自动填充
     *
     * @return MetaObjectHandler
     */
    @Bean
    public MetaObjectHandler metaObjectHandler() {
        return new MyMetaObjectHandler();
    }

    /**
     * 自定义 MetaObjectHandler
     */
    public static class MyMetaObjectHandler implements MetaObjectHandler {

        @Override
        public void insertFill(MetaObject metaObject) {
            // 自动填充创建时间和更新时间字段
            this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
            this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        }

        @Override
        public void updateFill(MetaObject metaObject) {
            // 自动填充更新时间字段
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        }
    }

    /**
     * 插件相关
     *
     * @return interceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        // 乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }
}
