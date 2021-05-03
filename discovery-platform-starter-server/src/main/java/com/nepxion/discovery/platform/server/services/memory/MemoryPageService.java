package com.nepxion.discovery.platform.server.services.memory;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.entity.vo.Page;
import com.nepxion.discovery.platform.server.ineterfaces.PageService;

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

public class MemoryPageService implements PageService {

    @Override
    public void fillPages(Admin admin) {

    }

    @Override
    public List<SysPage> listEmptyUrlPages() {
        return null;
    }

    @Override
    public List<SysPage> listAll() {
        return null;
    }

    @Override
    public SysPage getById(Long id) {
        return null;
    }

    @Override
    public IPage<Page> list(String name, Integer pageNum, Integer pageSize) {
        return null;
    }

    @Override
    public Long getMaxOrderNum(Long parentId) {
        return null;
    }

    @Override
    public void insert(SysPage sysPage) {

    }

    @Override
    public void updateById(SysPage sysPage) {

    }

    @Override
    public void removeByIds(List<Long> idsList) {

    }
}
