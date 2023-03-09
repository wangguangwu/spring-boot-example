package com.wangguangwu.mybatisplus.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Collections;

/**
 * 代码生成器
 *
 * @author wangguangwu
 */
public class CodeGenerator {

    /**
     * 作者
     */
    private static final String AUTHOR = "wangguangwu";

    /**
     * 项目名称
     */
    private static final String PROJECT_NAME = "spring-boot-mybatis-plus";

    /**
     * 包名
     */
    private static final String PACKAGE_NAME = "com.wangguangwu.mybatisplus";

    /**
     * 文件所在目录
     */
    private static final String FILE_PATH = System.getProperty("user.dir");

    /**
     * 数据库配置
     */
    private static final String URL_PATH = "jdbc:mysql://127.0.0.1:3306/masterdb?useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        create();
    }

    private static void create() {
        // 创建的表名
        String tableName = "user";

        // 生成文件
        FastAutoGenerator.create(URL_PATH, USER_NAME, PASSWORD)
                .globalConfig(builder ->
                        builder
                                // 设置作者
                                .author(AUTHOR)
                                // 指定输出目录
                                .outputDir(FILE_PATH + File.separator + PROJECT_NAME + "/src/main/java")
                                // 生成完毕不打开文件夹
                                .disableOpenDir()
                )
                .packageConfig(builder ->
                        builder
                                // 设置父包名
                                .parent(PACKAGE_NAME)
                                // 设置不同文件的包名
                                .entity("entity")
                                .service("service")
                                .serviceImpl("service.impl")
                                .mapper("mapper")
                                // 设置 mapperXml 生成路径
                                .pathInfo(Collections.singletonMap(OutputFile.xml, PROJECT_NAME + "/src/main/resources/mapper"))
                )
                .strategyConfig(builder ->
                        builder
                                // 设置需要生成的表名
                                .addInclude(tableName)
                                // 设置过滤表前缀
                                .addTablePrefix("")
                                // 去掉Service的 "I" 前缀
                                .serviceBuilder().formatServiceFileName("%sService").fileOverride()
                                // 开启 lombok
                                .entityBuilder()
                                .formatFileName("%sDO").fileOverride()
                                .enableLombok()
                                // 开启生成实体时生成字段注解
                                .enableTableFieldAnnotation()
                                // 乐观锁字段名(数据库)
                                .versionColumnName("version")
                                // 乐观锁属性名(实体)
                                .versionPropertyName("version")
                                // 逻辑删除字段名(数据库)
                                .logicDeleteColumnName("is_deleted")
                                // 逻辑删除属性名(实体)
                                .logicDeletePropertyName("deleted")
                                // 数据库表映射到实体的命名策略 -- 下划线转驼峰命名
                                .naming(NamingStrategy.underline_to_camel)
                                // 数据库表字段映射到实体的命名策略 -- 下划线转驼峰命名
                                .columnNaming(NamingStrategy.underline_to_camel)
                                // 覆盖原文件
                                .mapperBuilder().fileOverride()
                )
                // 自定义模板，在实体类上加上 @Data 注解
                // 不需要可以注释掉
                // 如果没有走步骤 2，需要省去这行代码
                .templateConfig(builder -> builder.entity("templates/entity.java"))
                // 使用 Freemarker 引擎模板，默认的是 Velocity 引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                // 禁止生成此文件
                .templateConfig(builder -> builder.controller(""))
                .execute();
    }
}
