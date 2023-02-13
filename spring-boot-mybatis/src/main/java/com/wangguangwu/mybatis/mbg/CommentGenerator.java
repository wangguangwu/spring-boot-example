package com.wangguangwu.mybatis.mbg;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * @author wangguangwu
 */
public class CommentGenerator extends DefaultCommentGenerator {

    private boolean addRemarkComments = false;

    /**
     * 设置用户配置的参数
     */
    @Override
    public void addConfigurationProperties(Properties properties) {
        super.addConfigurationProperties(properties);
        this.addRemarkComments = StringUtility.isTrue(properties.getProperty("addRemarkComments"));
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        String author = "wangguangwu";

        // 获取表注释
        String remarks = introspectedTable.getRemarks();

        topLevelClass.addJavaDocLine("/**");
        if (StringUtils.isNotBlank(remarks)) {
            topLevelClass.addJavaDocLine(" * " + remarks);
        }
        topLevelClass.addJavaDocLine(" *");
        topLevelClass.addJavaDocLine(" * @author " + author);
        topLevelClass.addJavaDocLine(" */");
    }

    /**
     * 给字段添加注释
     */
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable,
                                IntrospectedColumn introspectedColumn) {
        String remarks = introspectedColumn.getRemarks();
        // 根据参数和备注信息判断是否添加备注信息
        if (addRemarkComments && StringUtility.stringHasValue(remarks)) {
            addFieldJavaDoc(field, remarks);
        }
    }

    /**
     * 给 model 的字段添加注释
     */
    private void addFieldJavaDoc(Field field, String remarks) {
        // 文档注释开始
        field.addJavaDocLine("/**");
        // 获取数据库字段的备注信息
        String[] remarkLines = remarks.split(System.getProperty("line.separator"));
        for (String remarkLine : remarkLines) {
            if (StringUtils.isNotBlank(remarkLine)) {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        addJavadocTag(field, false);
        field.addJavaDocLine(" */");
    }
}
