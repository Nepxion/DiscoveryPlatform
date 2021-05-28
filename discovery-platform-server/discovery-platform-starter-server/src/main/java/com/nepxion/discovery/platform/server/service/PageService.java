package com.nepxion.discovery.platform.server.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.List;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;

public interface PageService {
    void fillPages(AdminVo adminVo) throws Exception;

    List<SysPageDto> listEmptyUrlPages() throws Exception;

    List<SysPageDto> listNotEmptyUrlPages() throws Exception;

    IPage<PageVo> list(@Nullable String name, Integer pageNum, Integer pageSize) throws Exception;

    Long getMaxOrder(Long parentId) throws Exception;

    List<SysPageDto> list();

    boolean insert(SysPageDto sysPage);

    SysPageDto getById(Long id);

    boolean updateById(SysPageDto sysPage);

    boolean removeByIds(Set<Long> idList);
}