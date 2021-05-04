package com.nepxion.discovery.platform.tool.db;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.*;
import com.nepxion.discovery.platform.tool.exception.ExceptionTool;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.*;
import java.util.stream.Collectors;

public final class SqlTool {
    public static String removeMySqlQualifier(final String source) {
        return removeQualifier(source, "`");
    }

    public static String removeMySqlCharQualifier(final String source) {
        return removeQualifier(source, "'");
    }

    public static String removeQualifier(final String source, final String qualifier) {
        if (null == source) {
            return null;
        }
        final String one = StrUtil.removeSuffix(source, qualifier);
        final String result = StrUtil.removePrefix(one, qualifier);
        return result.trim();
    }

    public static List<SQLStatement> analyseMySql(final String sql) {
        try {
            return SQLUtils.parseStatements(sql, DbType.mysql, false);
        } catch (Exception e) {
            throw new RuntimeException(ExceptionTool.getRootCauseMessage(e));
        }
    }

    public static void getWhereFieldAndValue(final SQLExpr sqlExpr, final Map<String, List<Object>> fieldAndValue) {
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            final SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;

            final SQLExpr right = sqlBinaryOpExpr.getRight();
            if (right instanceof SQLBinaryOpExpr) {
                final SQLBinaryOpExpr innerRight = (SQLBinaryOpExpr) right;
                if (innerRight.getRight() instanceof SQLBinaryOpExpr) {
                    getWhereFieldAndValue(innerRight.getRight(), fieldAndValue);
                } else {
                    fieldAndValue.put(
                            SqlTool.removeMySqlQualifier(innerRight.getLeft().toString()),
                            Collections.singletonList(getValueBySqlExpr(innerRight.getRight())));
                }
                getWhereFieldAndValue(sqlBinaryOpExpr.getLeft(), fieldAndValue);
            } else if (right instanceof SQLInListExpr) {
                final SQLInListExpr innerRight = (SQLInListExpr) right;
                fieldAndValue.put(
                        SqlTool.removeMySqlQualifier(innerRight.getExpr().toString()),
                        innerRight.getTargetList().stream().map(SqlTool::getValueBySqlExpr).collect(Collectors.toList()));
                getWhereFieldAndValue(((SQLBinaryOpExpr) innerRight.getParent()).getLeft(), fieldAndValue);
            } else {
                fieldAndValue.put(
                        SqlTool.removeMySqlQualifier(sqlBinaryOpExpr.getLeft().toString()),
                        Collections.singletonList(getValueBySqlExpr(sqlBinaryOpExpr.getRight())));
            }
        } else if (sqlExpr instanceof SQLInListExpr) {
            final SQLInListExpr sqlInListExpr = (SQLInListExpr) sqlExpr;
            fieldAndValue.put(
                    SqlTool.removeMySqlQualifier(sqlInListExpr.getExpr().toString()),
                    sqlInListExpr.getTargetList().stream().map(SqlTool::getValueBySqlExpr).collect(Collectors.toList()));
        }
    }

    public static String getSql(final Invocation invocation) throws Exception {
        final String question = URLEncoder.encode("?", StandardCharsets.UTF_8.name());

        final MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        final Configuration configuration = mappedStatement.getConfiguration();
        final BoundSql boundSql = getBoundSql(invocation);

        final Object parameterObject = boundSql.getParameterObject();
        final List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = URLEncoder.encode(boundSql.getSql().replaceAll("[\\s]+", " "), StandardCharsets.UTF_8.name())
                .replaceAll(question, "\\?");
        if (!parameterMappings.isEmpty() && null != parameterObject) {
            final TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst(question, URLEncoder.encode(getParameterValue(parameterObject), StandardCharsets.UTF_8.name()));
            } else {
                final MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (final ParameterMapping parameterMapping : parameterMappings) {
                    final String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        sql = sql.replaceFirst("\\?", URLEncoder.encode(getParameterValue(obj), StandardCharsets.UTF_8.name()));
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        sql = sql.replaceFirst("\\?", URLEncoder.encode(getParameterValue(obj), StandardCharsets.UTF_8.name()));
                    }
                }
            }
        }
        return URLDecoder.decode(sql.trim(), StandardCharsets.UTF_8.name());
    }

    public static Object getValueBySqlExpr(final SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLNullExpr) {
            return null;
        } else if (sqlExpr instanceof SQLCharExpr) {
            return SqlTool.removeMySqlCharQualifier(((SQLCharExpr) sqlExpr).getValue().toString());
        } else if (sqlExpr instanceof SQLIntegerExpr) {
            return ((SQLIntegerExpr) sqlExpr).getValue();
        }
        return sqlExpr.toString().trim();
    }

    private static BoundSql getBoundSql(final Invocation invocation) {
        final Object[] args = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        final Object parameterObject = args[1];
        return mappedStatement.getBoundSql(parameterObject);
    }

    private static String getParameterValue(final Object obj) {
        String value;
        if (obj instanceof String) {
            value = "'".concat(obj.toString()).concat("'");
        } else if (obj instanceof Date) {
            final DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'".concat(formatter.format(obj)).concat("'");
        } else {
            if (null != obj) {
                value = obj.toString();
            } else {
                value = "";
            }
        }
        return value;
    }
}