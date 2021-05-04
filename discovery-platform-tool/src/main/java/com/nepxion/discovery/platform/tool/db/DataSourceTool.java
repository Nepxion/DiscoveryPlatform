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

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collections;

public final class DataSourceTool {
    private static final Logger LOG = LoggerFactory.getLogger(DataSourceTool.class);

    private static final String DATA_BASE_URL = "jdbc:mysql://%s/%s?allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai&characterEncoding=UTF-8&useUnicode=true&autoReconnect=true&allowMultiQueries=true&useSSL=false&rewriteBatchedStatements=true&zeroDateTimeBehavior=CONVERT_TO_NULL";
    private final static String ZIPKIN_MYSQL8_INTERCEPTOR = "queryInterceptors=brave.mysql8.TracingQueryInterceptor&exceptionInterceptors=brave.mysql8.TracingExceptionInterceptor&zipkinServiceName=%s";
    private static final String CONNECTION_TEST_QUERY = "SELECT 1";
    private static final String CONNECTION_INIT_SQL = "SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci";
    private static final int INITIAL_SIZE = 5;
    private static final int MIN_IDLE = 10;
    private static final int MAXIMUM = 30;
    private static final String DRIVER_CLASS_NAME = com.mysql.cj.jdbc.Driver.class.getCanonicalName();

    public static DataSource createDruidDataSource(@Nullable final String poolName,
                                                   final String host,
                                                   @Nullable final String port,
                                                   final String databaseName,
                                                   final String userName,
                                                   final String password,
                                                   final Integer initialSize,
                                                   final Integer minIdle,
                                                   final Integer maxActive,
                                                   @Nullable final PostProcessor postProcessor) {
        final Parameter parameter = DataSourceTool.generateParameter(poolName, host, port, databaseName, postProcessor);
        final DruidDataSource result = new DruidDataSource();
        if (!ObjectUtils.isEmpty(parameter.getPoolName())) {
            result.setName(parameter.getPoolName()); //连接池名称
        }
        result.setDriverClassName(DRIVER_CLASS_NAME);
        result.setUrl(parameter.getUrl());
        result.setUsername(userName);
        result.setPassword(password);
        result.setInitialSize(initialSize); //配置初始化大小
        result.setMinIdle(minIdle); // 配置连接池中最小可用连接的个数
        result.setMaxActive(maxActive); //配置连接池中最大可用连接的个数
        result.setMaxWait(60000L); //配置获取连接等待超时的时间, 单位是毫秒
        result.setTimeBetweenEvictionRunsMillis(60000L); //配置间隔多久才进行一次检测, 检测需要关闭的空闲连接, 单位是毫秒
        result.setMinEvictableIdleTimeMillis(300000); //配置一个连接在池中最小生存的时间, 单位是毫秒
        result.setValidationQueryTimeout(60000);
        result.setTestWhileIdle(true);
        result.setTestOnBorrow(false);
        result.setTestOnReturn(false);
        result.setPoolPreparedStatements(false); //打开PSCache, 并且指定每个连接上PSCache的大小.分库分表较多的数据库，建议配置为false
        result.setMaxPoolPreparedStatementPerConnectionSize(20);
        result.setMaxOpenPreparedStatements(20);
        result.setValidationQuery(CONNECTION_TEST_QUERY);
        result.setConnectionInitSqls(Collections.singleton(CONNECTION_INIT_SQL));
        return result;
    }

    public static DataSource createDruidDataSource(final String host,
                                                   final String port,
                                                   final String databaseName,
                                                   final String userName,
                                                   final String password) {
        return createDruidDataSource(null, host, port, databaseName, userName, password, INITIAL_SIZE, MIN_IDLE, MAXIMUM, null);
    }

    public static DataSource createHikariDataSource(final String poolName,
                                                    final String host,
                                                    final String port,
                                                    final String databaseName,
                                                    final String userName,
                                                    final String password,
                                                    final Integer minIdle,
                                                    final Integer maximum,
                                                    @Nullable final PostProcessor postProcessor) {
        final Parameter parameter = DataSourceTool.generateParameter(poolName, host, port, databaseName, postProcessor);
        final HikariDataSource result = new HikariDataSource();
        if (!ObjectUtils.isEmpty(parameter.getPoolName())) {
            result.setPoolName(parameter.getPoolName()); //连接池名称
        }
        result.setDriverClassName(DRIVER_CLASS_NAME);
        result.setJdbcUrl(parameter.getUrl());
        result.setUsername(userName);
        result.setPassword(password);
        result.setMinimumIdle(minIdle);     //最小空闲连接数量
        result.setMaximumPoolSize(maximum); //连接池最大连接数，默认是10
        result.setIdleTimeout(600000); //空闲连接存活最大时间，默认600000（10分钟）
        result.setAutoCommit(true);  //此属性控制从池返回的连接的默认自动提交行为,默认值：true
        result.setMaxLifetime(1800000); //此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟
        result.setConnectionTimeout(30000); //数据库连接超时时间,默认30秒，即30000
        result.setConnectionTestQuery(CONNECTION_TEST_QUERY);
        result.setConnectionInitSql(CONNECTION_INIT_SQL);
        return result;
    }

    public static DataSource createHikariDataSource(final String poolName,
                                                    final String host,
                                                    final String port,
                                                    final String databaseName,
                                                    final String userName,
                                                    final String password,
                                                    final Integer minIdle,
                                                    final Integer maximum) {
        return createHikariDataSource(poolName, host, port, databaseName, userName, password, minIdle, maximum, null);
    }

    public static DataSource createHikariDataSource(final String host,
                                                    final String port,
                                                    final String databaseName,
                                                    final String userName,
                                                    final String password,
                                                    final Integer minIdle,
                                                    final Integer maximum) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, minIdle, maximum, null);
    }

    public static DataSource createHikariDataSource(final String host,
                                                    final String port,
                                                    final String databaseName,
                                                    final String userName,
                                                    final String password,
                                                    final PostProcessor postProcessor) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, MIN_IDLE, MAXIMUM, postProcessor);
    }

    public static DataSource createHikariDataSource(final String host,
                                                    final String port,
                                                    final String databaseName,
                                                    final String userName,
                                                    final String password) {
        return createHikariDataSource(null, host, port, databaseName, userName, password, MIN_IDLE, MAXIMUM, null);
    }

    public static Parameter registerZipkinForMySql8(final Parameter parameter) {
        String name = ObjectUtils.isEmpty(parameter.getPoolName()) ? parameter.getDatabaseName() : parameter.getPoolName();
        if (ObjectUtils.isEmpty(name)) {
            name = Thread.currentThread().getName();
        }
        final String zipkinServiceName = String.format("MYSQL_%s", name).toUpperCase();
        final String suffix = String.format(ZIPKIN_MYSQL8_INTERCEPTOR, zipkinServiceName);
        parameter.setUrl(parameter.getUrl().concat(String.format("&%s", suffix)));
        return parameter;
    }

    public static void close(final DataSource dataSource) {
        if (null != dataSource) {
            try {
                if (dataSource instanceof DruidDataSource) {
                    ((DruidDataSource) dataSource).close();
                } else if (dataSource instanceof HikariDataSource) {
                    ((HikariDataSource) dataSource).close();
                }

            } catch (final Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(final Connection connection) {
        if (null != connection) {
            try {
                connection.close();
            } catch (final Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(final Statement statement) {
        if (null != statement) {
            try {
                statement.close();
            } catch (final Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    public static void close(final ResultSet resultSet) {
        if (null != resultSet) {
            try {
                resultSet.close();
            } catch (final Exception exception) {
                LOG.error(exception.getMessage(), exception);
            }
        }
    }

    private static String getDbUrl(final String host,
                                   final String port) {
        if (ObjectUtils.isEmpty(host)) {
            throw new RuntimeException("host is required");
        }

        if (ObjectUtils.isEmpty(port)) {
            return host.trim();
        } else {
            return String.format("%s:%s", host.trim(), port.trim());
        }
    }

    private static Parameter generateParameter(final String poolName,
                                               final String host,
                                               @Nullable final String port,
                                               final String databaseName,
                                               final PostProcessor postProcessor) {
        Parameter result = new Parameter();
        result.setPoolName(poolName);
        result.setHost(host);
        result.setPort(port);
        result.setDatabaseName(databaseName);
        result.setUrl(String.format(DATA_BASE_URL, DataSourceTool.getDbUrl(host, port), databaseName));
        if (null != postProcessor) {
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