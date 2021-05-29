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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;
import com.nepxion.discovery.platform.server.mysql.mapper.MySqlPageMapper;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class MySqlPageService extends ServiceImpl<MySqlPageMapper, SysPageDto> implements PageService {
    @TransactionReader
    @Override
    public List<SysPageDto> list() {
        return super.list();
    }

    @TransactionReader
    @Override
    public void fillPages(AdminVo adminVo) {
        if (adminVo == null) {
            return;
        }
        adminVo.setPermissions(getPages(adminVo));
        if (adminVo.getPermissions() != null) {
            adminVo.getPermissions().sort((o1, o2) -> (int) (o1.getOrder() - o2.getOrder()));
        }
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public List<SysPageDto> listEmptyUrlPages() {
        QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPageDto::getUrl, "")
                .orderByAsc(SysPageDto::getName);
        return list(queryWrapper);
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public List<SysPageDto> listNotEmptyUrlPages() {
        QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(SysPageDto::getUrl, "")
                .orderByAsc(SysPageDto::getName);
        return list(queryWrapper);
    }

    @TransactionReader
    @Override
    public IPage<PageVo> list(String name, Integer pageNum, Integer pageSize) {
        return baseMapper.list(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), name);
    }

    @TransactionReader
    @Override
    public Long getMaxOrder(Long parentId) {
        return baseMapper.getMaxOrder(parentId);
    }

    @TransactionWriter
    @Override
    public boolean insert(SysPageDto sysPage) {
        return super.save(sysPage);
    }

    @TransactionReader
    @Override
    public SysPageDto getById(Long id) {
        return super.getById(id);
    }

    @TransactionWriter
    @Override
    public boolean updateById(SysPageDto entity) {
        return super.updateById(entity);
    }

    @TransactionWriter
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    @SuppressWarnings("unchecked")
    private List<SysPageDto> listSysPages() {
        QueryWrapper<SysPageDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPageDto::getIsMenu, 1)
                .orderByAsc(SysPageDto::getParentId, SysPageDto::getOrder);
        return list(queryWrapper);
    }

    private List<PageVo> listPermissionPages(Long adminId) {
        return baseMapper.listPermissionPages(adminId);
    }

    private List<PageVo> getPages(AdminVo adminVo) {
        List<SysPageDto> allPages = listSysPages();
        Map<Long, SysPageDto> allPageMap = toMap(allPages);

        // 非顶级集合
        List<PageVo> nonRootPageList = new ArrayList<>();
        // 顶级集合
        List<PageVo> rootPageList = new ArrayList<>();
        List<PageVo> pageVoList = listPermissionPages(adminVo.getId());

        Map<Long, PageVo> permission = new HashMap<>((int) (0.75 / pageVoList.size()));
        for (PageVo pageVo : pageVoList) {
            permission.put(pageVo.getId(), pageVo);
        }

        for (SysPageDto sysPage : allPages) {
            if (!adminVo.getSysRole().getSuperAdmin() && !permission.containsKey(sysPage.getId())) {
                continue;
            }
            if (0 == sysPage.getParentId()) {
                PageVo rootPageVo = CommonTool.toVo(sysPage, PageVo.class);
                PageVo permissionPageVo = permission.get(rootPageVo.getId());
                if (permissionPageVo != null) {
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

        for (PageVo pageVo : nonRootPageList) {
            SysPageDto sysPageParent = allPageMap.get(pageVo.getParentId());
            if (sysPageParent != null) {
                Optional<PageVo> first = rootPageList.stream().filter(p -> p.getId().equals(sysPageParent.getId())).findFirst();
                if (!first.isPresent()) {
                    PageVo rootPageVo = CommonTool.toVo(sysPageParent, PageVo.class);
                    PageVo permissionPageVo = permission.get(rootPageVo.getId());
                    if (permissionPageVo != null) {
                        rootPageVo.setCanInsert(permissionPageVo.getCanInsert());
                        rootPageVo.setCanDelete(permissionPageVo.getCanDelete());
                        rootPageVo.setCanUpdate(permissionPageVo.getCanUpdate());
                        rootPageVo.setCanSelect(permissionPageVo.getCanSelect());
                    }
                    rootPageList.add(rootPageVo);
                }
            }
        }

        if (CollectionUtils.isNotEmpty(rootPageList) || CollectionUtils.isNotEmpty(nonRootPageList)) {
            Set<Long> map = Sets.newHashSetWithExpectedSize(nonRootPageList.size());
            rootPageList.forEach(rootPage -> getChild(adminVo, permission, rootPage, nonRootPageList, map));
            filter(rootPageList);
            return rootPageList;
        }
        return null;
    }

    private Map<Long, SysPageDto> toMap(List<SysPageDto> sysPageList) {
        Map<Long, SysPageDto> result = new HashMap<>((int) (sysPageList.size() / 0.75));

        for (SysPageDto sysPage : sysPageList) {
            result.put(sysPage.getId(), sysPage);
        }
        return result;
    }

    private void filter(List<PageVo> pageList) {
        Iterator<PageVo> iterator = pageList.iterator();
        while (iterator.hasNext()) {
            PageVo page = iterator.next();
            if (StringUtils.isEmpty(page.getUrl()) && (page.getChildren() == null || page.getChildren().size() < 1)) {
                iterator.remove();
            } else {
                filter(page.getChildren());
            }
        }
    }

    private void getChild(AdminVo adminVo, Map<Long, PageVo> permission, PageVo parentPage, List<PageVo> childrenPageList, Set<Long> set) {
        List<PageVo> childList = Lists.newArrayList();
        childrenPageList.stream().//
                filter(p -> !set.contains(p.getId())). // 判断是否已循环过当前对象
                filter(p -> p.getParentId().equals(parentPage.getId())). // 判断是否父子关系
                filter(p -> set.size() <= childrenPageList.size()).// set集合大小不能超过childrenPageList的大小
                forEach(p -> {
            if (adminVo.getSysRole().getSuperAdmin() || StringUtils.isEmpty(p.getUrl()) || permission.containsKey(p.getId())) {
                // 放入set, 递归循环时可以跳过这个页面，提高循环效率
                set.add(p.getId());
                // 递归获取当前类目的子类目
                getChild(adminVo, permission, p, childrenPageList, set);

                if (permission.containsKey(p.getId())) {
                    PageVo page = permission.get(p.getId());
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