package com.nepxion.discovery.platform.server.mysql.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

import com.zaxxer.hikari.HikariDataSource;

public class PlatformMySqlTool {
    private static final Logger LOG = LoggerFactory.getLogger(PlatformMySqlTool.class);

    private static final String DATA_BASE_URL = "jdbc:mysql://%s/%s?allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&useUnicode=true&autoReconnect=true&allowMultiQueries=true&useSSL=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=CONVERT_TO_NULL";
    private static final String ZIPKIN_MYSQL8_INTERCEPTOR = "queryInterceptors=brave.mysql8.TracingQueryInterceptor&exceptionInterceptors=brave.mysql8.TracingExceptionInterceptor&zipkinServiceName=%s";
    private static final String CONNECTION_TEST_QUERY = "SELECT 1";
    private static final String CONNECTION_INIT_SQL = "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci";
    private static final int MIN_IDLE = 10;
    private static final int MAXIMUM = 30;
    private static final String DRIVER_CLASS_NAME = com.mysql.cj.jdbc.Driver.class.getCanonicalName();

    public static DataSource createHikariDataSource(String poolName, String host, String port, String databaseName, String userName, String password, Integer minIdle, Integer maximum, @Nullable PostProcessor postProcessor) {
        Parameter parameter = generateParameter(poolName, host, port, databaseName, postProcessor);
        HikariDataSource result = new HikariDataSource();
        if (StringUtils.isNotEmpty(parameter.getPoolName())) {
            result.setPoolName(parameter.getPoolName()); //连接池名称
        }
        result.setDriverClassName(DRIVER_CLASS_NAME);
        result.setJdbcUrl(parameter.getUrl());
        result.setUsername(userName);
        result.setPassword(password);
        result.setMinimumIdle(minIdle); //最小空闲连接数量
        result.setMaximumPoolSize(maximum); //连接池最大连接数，默认是10
        result.setIdleTimeout(600000); //空闲连接存活最大时间，默认600000（10分钟）
        result.setAutoCommit(true); //此属性控制从池返回的连接的默认自动提交行为,默认值：true
        result.setMaxLifetime(1800000); //此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟
        result.setConnectionTimeout(30000); //数据库连接超时时间,默认30秒，即30000
        result.setConnectionTestQuery(CONNECTION_TEST_QUERY);
        result.setConnectionInitSql(CONNECTION_INIT_SQL);
        return result;
    }

    public static DataSource createHikariDataSource(String poolName, String host, String port, String databaseName, String userName, String password, Integer minIdle, Integer maximum) {
        return createHikariDataSource(poolName, host, port, databaseName, userName, password, minIdle, maximum, null);
    }

    public static DataSource createHikariDataSource(String host, String port, String databaseName, String userName, String password, Integer minIdle, Integer maximum) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, minIdle, maximum, null);
    }

    public static DataSource createHikariDataSource(String host, String port, String databaseName, String userName, String password, PostProcessor postProcessor) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, MIN_IDLE, MAXIMUM, postProcessor);
    }

    public static DataSource createHikariDataSource(String host, String port, String databaseName, String userName, String password) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, MIN_IDLE, MAXIMUM, null);
    }

    public static Parameter registerZipkinForMySql8(Parameter parameter) {
        String name = StringUtils.isEmpty(parameter.getPoolName()) ? parameter.getDatabaseName() : parameter.getPoolName();
        if (StringUtils.isEmpty(name)) {
            name = Thread.currentThread().getName();
        }
        String zipkinServiceName = String.format("MYSQL_%s", name).toUpperCase();
        String suffix = String.format(ZIPKIN_MYSQL8_INTERCEPTOR, zipkinServiceName);
        parameter.setUrl(parameter.getUrl().concat(String.format("&%s", suffix)));
        return parameter;
    }

    public static void close(DataSource dataSource) {
        if (dataSource != null) {
            try {
                if (dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                }

            } catch (Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    private static String getDbUrl(String host, String port) {
        if (StringUtils.isEmpty(host)) {
            throw new RuntimeException("host is required");
        }

        if (StringUtils.isEmpty(port)) {
            return host.trim();
        } else {
            return String.format("%s:%s", host.trim(), port.trim());
        }
    }

    private static Parameter generateParameter(String poolName, String host, @Nullable String port, String databaseName, PostProcessor postProcessor) {
        Parameter result = new Parameter();
        result.setPoolName(poolName);
        result.setHost(host);
        result.setPort(port);
        result.setDatabaseName(databaseName);
        result.setUrl(String.format(DATA_BASE_URL, getDbUrl(host, port), databaseName));
        if (postProcessor != null) {
            result = postProcessor.after(result);
        }
        return result;
    }

    public static class Parameter {
        private String poolName;
        private String host;
        private String port;
        private String databaseName;
        private String url;

        public String getPoolName() {
            return poolName;
        }

        public void setPoolName(String poolName) {
            this.poolName = poolName;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getDatabaseName() {
            return databaseName;
        }

        public void setDatabaseName(String databaseName) {
            this.databaseName = databaseName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public interface PostProcessor {
        Parameter after(Parameter parameter);
    }
}