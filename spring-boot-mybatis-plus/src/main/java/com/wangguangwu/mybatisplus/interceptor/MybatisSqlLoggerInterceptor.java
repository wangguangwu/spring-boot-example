package com.wangguangwu.mybatisplus.interceptor;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.DefaultReflectorFactory;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.text.DateFormat;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 自定义拦截器打印日志
 *
 * @author wangguangwu
 */
@Component
@Slf4j
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class MybatisSqlLoggerInterceptor implements Interceptor {

    //==================================常量====================================

    private static final String SPACE_REGEX = "\\s+";
    private static final String QUESTION_MARK_REGEX = "\\?";
    private static final String MISSING_PARAM = "缺失";

    //=================================重写方法===================================

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 获取 statementHandler 对象，用于获取 SQL 语句
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        MetaObject metaObject = MetaObject.forObject(statementHandler,
                SystemMetaObject.DEFAULT_OBJECT_FACTORY,
                SystemMetaObject.DEFAULT_OBJECT_WRAPPER_FACTORY,
                new DefaultReflectorFactory());
        // 获取 mappedStatement 对象，用于获取 SQL 语句类型
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        // 获取 SQL 语句类型
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();
        // 获取 boundSql 对象，用于获取 SQL 语句的参数
        BoundSql boundSql = statementHandler.getBoundSql();

        // 获取到最终执行的 sql 语句
        String ultimateSql = getSql(mappedStatement.getConfiguration(), boundSql);
        log.info("Execute SQL: \n{}", ultimateSql);

        // 记录执行时间
        long start = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        log.info("SQL Type: {}, Execute Time: {}", sqlCommandType, (System.currentTimeMillis() - start));
        return returnValue;
    }

    /**
     * 包装对象
     *
     * @param target 被包装对象
     * @return 包装后的对象
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    /**
     * 设置拦截器的属性
     *
     * @param properties 属性信息
     */
    @Override
    public void setProperties(Properties properties) {
        // ignore
    }

    //=================================私有方法===================================

    /**
     * 获取参数值的字符串表示形式
     */
    private String getParameterValue(Object obj) {
        return Optional.ofNullable(obj)
                .map(o -> {
                    if (o instanceof String) {
                        return "'" + o + "'";
                    } else if (o instanceof Date) {
                        DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
                        return "'" + formatter.format(new Date()) + "'";
                    } else {
                        return o.toString();
                    }
                })
                .orElse("");
    }

    /**
     * 根据参数生成完整的 SQL
     *
     * @param configuration Mybatis配置
     * @param boundSql      SQL信息
     * @return 完整的 SQL
     */
    private String getSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll(SPACE_REGEX, " ");
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst(QUESTION_MARK_REGEX, Matcher.quoteReplacement(getParameterValue(parameterObject)));
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst(QUESTION_MARK_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst(QUESTION_MARK_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst(QUESTION_MARK_REGEX, MISSING_PARAM);
                    }
                }
            }
        }
        return sql;
    }
}
