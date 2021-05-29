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

import java.util.List;
import java.util.Set;

import org.springframework.lang.Nullable;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.po.MenuPo;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.MenuVo;

public interface MenuService {
    void fillPages(AdminVo adminVo) throws Exception;

    List<SysMenuDto> listEmptyUrlMenus() throws Exception;

    List<SysMenuDto> listNotEmptyUrlMenus() throws Exception;

    IPage<MenuVo> list(@Nullable String name, Integer pageNum, Integer pageSize) throws Exception;

    Long getMaxOrder(Long parentId) throws Exception;

    List<SysMenuDto> list();

    boolean insert(MenuPo menuPo) throws Exception;

    SysMenuDto getById(Long id);

    boolean updateById(MenuPo menuPo) throws Exception;

    boolean removeByIds(Set<Long> idList);
}