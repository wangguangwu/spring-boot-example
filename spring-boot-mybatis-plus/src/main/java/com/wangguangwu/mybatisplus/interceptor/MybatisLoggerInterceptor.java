package com.wangguangwu.mybatisplus.interceptor;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;

/**
 * 自定义拦截器打印完整的执行 sql
 *
 * @author wangguangwu
 */
@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class})
})
@Slf4j
public class MybatisLoggerInterceptor implements Interceptor {


    //==================================常量====================================

    private static final String SPACE_REGEX = "\\s+";
    private static final String QUESTION_MARK_REGEX = "\\?";
    private static final int INDEX_TWO = 2;
    private static final int INDEX_FIVE = 5;
    private static final int INDEX_SIX = 6;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        // 参数
        Object[] args = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) args[0];
        Object parameter = args[1];
        // sql 类型
        String sqlCommandType = mappedStatement.getSqlCommandType().toString();

        if (parameter == null) {
            return invocation.proceed();
        }

        // 分页，仅支持 PageHelper
        if (args.length == INDEX_SIX && args[INDEX_TWO] instanceof RowBounds) {
            BoundSql boundSql = (BoundSql) args[INDEX_FIVE];
            String formattedSql = getSql(mappedStatement.getConfiguration(), boundSql);
            log.info("Executing SQL: {}", formattedSql);
            return invocation.proceed();
        }

        // 获取原始 sql 语句
        BoundSql originalSql = mappedStatement.getBoundSql(parameter);
        String formattedSql = getSql(mappedStatement.getConfiguration(), originalSql);

        // 打印完整的 SQL 语句
        log.info("Executing SQL: {}", formattedSql);

        // Measure execution time
        long start = System.currentTimeMillis();
        Object returnValue = invocation.proceed();
        log.info("SQL Type: {}, Execute Time: {}", sqlCommandType, (System.currentTimeMillis() - start));
        return returnValue;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }

    //=============================私有方法=========================

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
     * 拼接 sql
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
                        // 处理自动填充的字段
                        obj = checkFill(parameterObject, propertyName, obj);
                        sql = sql.replaceFirst(QUESTION_MARK_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst(QUESTION_MARK_REGEX, Matcher.quoteReplacement(getParameterValue(obj)));
                    }
                }
            }
        }
        return sql;
    }

    private Object checkFill(Object parameterObject, String propertyName, Object obj) {
        Field field;
        try {
            field = parameterObject.getClass().getDeclaredField(propertyName);
        } catch (NoSuchFieldException e) {
            return obj;
        }
        FieldFill fill = getFieldFill(field);
        Class<?> type = field.getType();
        // 是插入或者更新类型，并且是时间字段
        boolean time = (type == LocalDateTime.class || type == Date.class)
                && (fill == FieldFill.INSERT || fill == FieldFill.UPDATE);
        return time && obj == null ? LocalDateTime.now() : obj;
    }

    private FieldFill getFieldFill(Field field) {
        TableField tableField = field.getAnnotation(TableField.class);
        if (tableField != null) {
            return tableField.fill();
        }
        return FieldFill.DEFAULT;
    }
}

