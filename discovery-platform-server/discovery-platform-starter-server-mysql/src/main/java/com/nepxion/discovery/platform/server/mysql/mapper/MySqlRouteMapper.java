package com.nepxion.discovery.platform.server.mysql.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MySqlRouteMapper {
    Integer getNextMaxCreateTimesInDay(@Param("tableName") String tableName);
}