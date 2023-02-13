package com.wangguangwu.mybatis.mbg;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangguangwu
 */
public class Generator {

    public static void main(String[] args) throws InvalidConfigurationException, SQLException, IOException, InterruptedException {
        // 记录警告信息
        List<String> warnings = new ArrayList<>();
        // 覆盖原代码，设置为覆盖
        boolean overwrite = true;
        // 读取配置文件
        Configuration config;
        try (InputStream is = Generator.class.getResourceAsStream("/generatorConfig.xml")) {
            ConfigurationParser cp = new ConfigurationParser(warnings);
            config = cp.parseConfiguration(is);
        } catch (IOException | XMLParserException e) {
            e.printStackTrace();
            return;
        }

        // 创建回调函数
        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        // 创建代码生成器
        MyBatisGenerator generator = new MyBatisGenerator(config, callback, warnings);
        // 生成代码
        // 注释中一定要保留 @mbggenerated ,MBG通过该字符串来判断代码是否为代码生成器生成的代码，有该标记的的代码在重新生成的时候会被删除
        generator.generate(null);
        // 输出警告信息
        warnings.forEach(System.out::println);
    }
}
