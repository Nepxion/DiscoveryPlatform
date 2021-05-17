package com.nepxion.discovery.platform.server.mysql.service;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlPageMapper;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.tool.anno.TranRead;
import com.nepxion.discovery.platform.server.tool.anno.TranSave;
import com.nepxion.discovery.platform.server.tool.common.CommonTool;
import org.springframework.stereotype.Service;

import java.util.*;

public class MySqlPageService extends ServiceImpl<MySqlPageMapper, SysPageDto> implements PageService {
    @TranRead
    @Override
    public List<SysPageDto> list() {
        return super.list();
    }

    @TranRead
    @Override
    public void fillPages(final AdminVo adminVo) {
        if (null == adminVo) {
            return;
        }
        adminVo.setPermissions(getPages(adminVo));
        if (null != adminVo.getPermissions()) {
            adminVo.getPermissions().sort((o1, o2) -> (int) (o1.getOrder() - o2.getOrder()));
        }
    }

    @TranRead
    @Override
    public List<SysPageDto> listEmptyUrlPages() {
        final QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPageDto::getUrl, "")
                .orderByAsc(SysPageDto::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public List<SysPageDto> listNotEmptyUrlPages() {
        final QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(SysPageDto::getUrl, "")
                .orderByAsc(SysPageDto::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public IPage<PageVo> list(final String name,
                              final Integer pageNum,
                              final Integer pageSize) {
        return this.baseMapper.list(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), name);
    }

    @TranRead
    @Override
    public Long getMaxOrder(final Long parentId) {
        return this.baseMapper.getMaxOrder(parentId);
    }

    @TranSave
    @Override
    public boolean insert(final SysPageDto sysPage) {
        return super.save(sysPage);
    }

    @TranRead
    @Override
    public SysPageDto getById(final Long id) {
        return super.getById(id);
    }

    @TranSave
    @Override
    public boolean updateById(final SysPageDto entity) {
        return super.updateById(entity);
    }

    @TranSave
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    private List<SysPageDto> listSysPages() {
        final QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPageDto::getIsMenu, 1)
                .orderByAsc(SysPageDto::getParentId, SysPageDto::getOrder);
        return this.list(queryWrapper);
    }

    private List<PageVo> listPermissionPages(final Long adminId) {
        return this.baseMapper.listPermissionPages(adminId);
    }

    private List<PageVo> getPages(final AdminVo adminVo) {
        final List<SysPageDto> allPages = this.listSysPages();
        final Map<Long, SysPageDto> allPageMap = toMap(allPages);

        // 非顶级集合
        final List<PageVo> nonRootPageList = new ArrayList<>();
        // 顶级集合
        final List<PageVo> rootPageList = new ArrayList<>();
        final List<PageVo> pageVoList = this.listPermissionPages(adminVo.getId());

        final Map<Long, PageVo> permission = new HashMap<>((int) (0.75 / pageVoList.size()));
        for (final PageVo pageVo : pageVoList) {
            permission.put(pageVo.getId(), pageVo);
        }

        for (final SysPageDto sysPage : allPages) {
            if (!adminVo.getSysRole().getSuperAdmin() && !permission.containsKey(sysPage.getId())) {
                continue;
            }
            if (0 == sysPage.getParentId()) {
                final PageVo rootPageVo = CommonTool.toVo(sysPage, PageVo.class);
                final PageVo permissionPageVo = permission.get(rootPageVo.getId());
                if (null != permissionPageVo) {
                    rootPageVo.setCanInsert(permissionPageVo.getCanInsert());
                    rootPageVo.setCanDelete(permissionPageVo.getCanDelete());
                    rootPageVo.setCanUpdate(permissionPageVo.getCanUpdate());
                    rootPageVo.setCanSelect(permissionPageVo.getCanSelect());
                }
                rootPageList.add(rootPageVo);
            } else {
                nonRootPageList.add(CommonTool.toVo(sysPage, PageVo.class));
            }
            if (sysPage.getIsDefault()) {
                adminVo.setDefaultPage(sysPage.getUrl());
            }
        }

        for (final PageVo pageVo : nonRootPageList) {
            final SysPageDto sysPageParent = allPageMap.get(pageVo.getParentId());
            if (null != sysPageParent) {
                final Optional<PageVo> first = rootPageList.stream().filter(p -> p.getId().equals(sysPageParent.getId())).findFirst();
                if (!first.isPresent()) {
                    final PageVo rootPageVo = CommonTool.toVo(sysPageParent, PageVo.class);
                    final PageVo permissionPageVo = permission.get(rootPageVo.getId());
                    if (null != permissionPageVo) {
                        rootPageVo.setCanInsert(permissionPageVo.getCanInsert());
                        rootPageVo.setCanDelete(permissionPageVo.getCanDelete());
                        rootPageVo.setCanUpdate(permissionPageVo.getCanUpdate());
                        rootPageVo.setCanSelect(permissionPageVo.getCanSelect());
                    }
                    rootPageList.add(rootPageVo);
                }
            }
        }

        if (ObjectUtils.isNotNull(rootPageList) || ObjectUtils.isNotNull(nonRootPageList)) {
            final Set<Long> map = Sets.newHashSetWithExpectedSize(nonRootPageList.size());
            rootPageList.forEach(rootPage -> getChild(adminVo, permission, rootPage, nonRootPageList, map));
            filter(rootPageList);
            return rootPageList;
        }
        return null;
    }

    private Map<Long, SysPageDto> toMap(final List<SysPageDto> sysPageList) {
        final Map<Long, SysPageDto> result = new HashMap<>((int) (sysPageList.size() / 0.75));

        for (final SysPageDto sysPage : sysPageList) {
            result.put(sysPage.getId(), sysPage);
        }
        return result;
    }

    private void filter(final List<PageVo> pageList) {
        final Iterator<PageVo> iterator = pageList.iterator();
        while (iterator.hasNext()) {
            final PageVo page = iterator.next();
            if (ObjectUtils.isEmpty(page.getUrl()) && (page.getChildren() == null || page.getChildren().size() < 1)) {
                iterator.remove();
            } else {
                filter(page.getChildren());
            }
        }
    }

    private void getChild(final AdminVo adminVo,
                          final Map<Long, PageVo> permission,
                          final PageVo parentPage,
                          final List<PageVo> childrenPageList,
                          final Set<Long> set) {
        final List<PageVo> childList = Lists.newArrayList();
        childrenPageList.stream().//
                filter(p -> !set.contains(p.getId())). // 判断是否已循环过当前对象
                filter(p -> p.getParentId().equals(parentPage.getId())). // 判断是否父子关系
                filter(p -> set.size() <= childrenPageList.size()).// set集合大小不能超过childrenPageList的大小
                forEach(p -> {
            if (adminVo.getSysRole().getSuperAdmin() || ObjectUtils.isEmpty(p.getUrl()) || permission.containsKey(p.getId())) {
                // 放入set, 递归循环时可以跳过这个页面，提高循环效率
                set.add(p.getId());
                // 递归获取当前类目的子类目
                getChild(adminVo, permission, p, childrenPageList, set);

                if (permission.containsKey(p.getId())) {
                    final PageVo page = permission.get(p.getId());
                    p.setCanInsert(page.getCanInsert());
                    p.setCanDelete(page.getCanDelete());
                    p.setCanUpdate(page.getCanUpdate());
                    p.setCanSelect(page.getCanSelect());
                }
                childList.add(p);
            }
        });
        parentPage.setChildren(childList);
    }
}