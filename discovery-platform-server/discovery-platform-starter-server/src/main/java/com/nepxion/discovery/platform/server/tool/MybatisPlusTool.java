package com.nepxion.discovery.platform.server.tool;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.annotation.TableName;

public class MybatisPlusTool {
    public static String getTableName(Class<?> tClass) {
        TableName tableName = tClass.getAnnotation(TableName.class);
        if (tableName == null) {
            return StringUtils.EMPTY;
        }
        return tableName.value();
    }
}