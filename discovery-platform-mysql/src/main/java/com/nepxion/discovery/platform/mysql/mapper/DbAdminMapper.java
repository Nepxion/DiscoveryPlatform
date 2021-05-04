package com.nepxion.discovery.platform.mysql.mapper;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.common.entity.dto.SysAdmin;
import com.nepxion.discovery.common.entity.vo.Admin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DbAdminMapper extends BaseMapper<SysAdmin> {
    IPage<Admin> list(IPage<Admin> page,
                      @Param("mode") int mode,
                      @Param("name") String name);
}