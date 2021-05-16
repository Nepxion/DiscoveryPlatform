package com.nepxion.discovery.platform.server.service;


/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Set;

public interface PageService {
    void fillPages(final AdminVo adminVo) throws Exception;

    List<SysPageDto> listEmptyUrlPages() throws Exception;

    List<SysPageDto> listNotEmptyUrlPages() throws Exception;

    IPage<PageVo> list(@Nullable final String name,
                       final Integer pageNum,
                       final Integer pageSize) throws Exception;

    Long getMaxOrder(final Long parentId) throws Exception;

    List<SysPageDto> list();

    boolean insert(final SysPageDto sysPage);

    SysPageDto getById(final Long id);

    boolean updateById(final SysPageDto sysPage);

    boolean removeByIds(final Set<Long> idList);
}
