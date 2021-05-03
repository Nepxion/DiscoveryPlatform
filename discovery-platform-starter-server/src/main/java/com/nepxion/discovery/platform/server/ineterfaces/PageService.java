package com.nepxion.discovery.platform.server.ineterfaces;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.entity.vo.Page;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public interface PageService {
    void fillPages(final Admin admin) throws Exception;

    List<SysPage> listEmptyUrlPages() throws Exception;

    List<SysPage> listAll() throws Exception;

    SysPage getById(final Long id) throws Exception;

    IPage<Page> list(@Nullable final String name,
                     final Integer pageNum,
                     final Integer pageSize) throws Exception;

    Long getMaxOrderNum(final Long parentId) throws Exception;

    void insert(final SysPage sysPage) throws Exception;

    void updateById(final SysPage sysPage) throws Exception;

    void removeByIds(final List<Long> idsList) throws Exception;
}
