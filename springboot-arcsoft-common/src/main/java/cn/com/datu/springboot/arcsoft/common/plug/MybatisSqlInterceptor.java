package com.zheng.common.plugin.ets;


import cn.com.datu.springboot.arcsoft.common.plug.MybatisSqlInterceptorConfig;
import cn.com.datu.springboot.arcsoft.common.plug.MybatisSqlInterceptorUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;

import static cn.com.datu.springboot.arcsoft.common.plug.MybatisSqlInterceptorUtil.SQL_SUFFIX;


@Component
@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisSqlInterceptor implements Interceptor {

    private static Logger LOGGER = LoggerFactory.getLogger(MybatisSqlInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (MybatisSqlInterceptorConfig.isPlugin_enable()) {
            try {
                MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];  // 获取xml中的一个select/update/insert/delete节点，主要描述的是一条SQL语句
                Object parameter = null;
                // 获取参数，if语句成立，表示sql语句有参数，参数格式是map形式
                if (invocation.getArgs().length > 1) {
                    parameter = invocation.getArgs()[1];
                }
                List<String> sqlIds = MybatisSqlInterceptorConfig.getSqlIds();
                if (!CollectionUtils.isEmpty(sqlIds) && sqlIds.contains(mappedStatement.getId())) {
                    BoundSql boundSql = mappedStatement.getBoundSql(parameter);  // BoundSql就是封装myBatis最终产生的sql类
                    Configuration configuration = mappedStatement.getConfiguration();  // 获取节点的配置
                    MybatisSqlInterceptorUtil.saveSqlToFile(showSql(configuration, boundSql));
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return invocation.proceed();  // 执行完上面的任务后，不改变原有的sql执行过程
    }

    /*<br>    *如果参数是String，则添加单引号， 如果是日期，则转换为时间格式器并加单引号； 对参数是null和不是null的情况作了处理<br>　　*/
    private static String getParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format(new Date()) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "''";
            }

        }
        return value;
    }

    // 进行？的替换
    public static String showSql(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();  // 获取参数
        List<ParameterMapping> parameterMappings = boundSql
                .getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");  // sql语句中多个空格都用一个空格代替
        if (CollectionUtils.isNotEmpty(parameterMappings) && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry(); // 获取类型处理器注册器，类型处理器的功能是进行java类型和数据库类型的转换<br>　　　　　　　// 如果根据parameterObject.getClass(）可以找到对应的类型，则替换
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(parameterObject)));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);// MetaObject主要是封装了originalObject对象，提供了get和set的方法用于获取和设置originalObject的属性值,主要支持对JavaBean、Collection、Map三种类型对象的操作
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);  // 该分支是动态sql
                        sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(getParameterValue(obj)));
                    } else {
                        sql = sql.replaceFirst("\\?", "缺少值");
                    }//打印出缺失，提醒该参数缺失并防止错位
                }
            }
        }
        sql = sql + SQL_SUFFIX;
        return sql;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
