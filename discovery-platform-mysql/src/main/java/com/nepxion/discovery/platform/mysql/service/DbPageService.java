package com.nepxion.discovery.platform.mysql.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nepxion.discovery.common.entity.dto.SysPage;
import com.nepxion.discovery.common.entity.vo.Admin;
import com.nepxion.discovery.common.entity.vo.Page;
import com.nepxion.discovery.common.interfaces.PageService;
import com.nepxion.discovery.platform.mysql.mapper.DbPageMapper;
import com.nepxion.discovery.platform.tool.anno.TranRead;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import com.nepxion.discovery.platform.tool.common.CommonTool;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

@Service
public class DbPageService extends ServiceImpl<DbPageMapper, SysPage> implements PageService {
    @TranRead
    @Override
    public List<SysPage> list() {
        return super.list();
    }

    @TranRead
    @Override
    public void fillPages(final Admin admin) {
        if (null == admin) {
            return;
        }
        admin.setPermissions(getPages(admin));
        if (null != admin.getPermissions()) {
            admin.getPermissions().sort((o1, o2) -> (int) (o1.getOrderNum() - o2.getOrderNum()));
        }
    }

    @TranRead
    @Override
    public List<SysPage> listEmptyUrlPages() {
        final QueryWrapper<SysPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPage::getUrl, "")
                .orderByAsc(SysPage::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public List<SysPage> listNotEmptyUrlPages() {
        final QueryWrapper<SysPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(SysPage::getUrl, "")
                .orderByAsc(SysPage::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public IPage<Page> list(final String name,
                            final Integer pageNum,
                            final Integer pageSize) {
        return this.baseMapper.list(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), name);
    }

    @TranRead
    @Override
    public Long getMaxOrderNum(final Long parentId) {
        return this.baseMapper.getMaxOrderNum(parentId);
    }

    @TranSave
    @Override
    public boolean insert(final SysPage sysPage) {
        return super.save(sysPage);
    }

    @TranRead
    @Override
    public SysPage getById(final Long id) {
        return super.getById(id);
    }

    @TranSave
    @Override
    public boolean updateById(final SysPage entity) {
        return super.updateById(entity);
    }

    @TranSave
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    private List<SysPage> listSysPages() {
        final QueryWrapper<SysPage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysPage::getIsMenu, 1)
                .orderByAsc(SysPage::getParentId, SysPage::getOrderNum);
        return this.list(queryWrapper);
    }

    private List<Page> listPermissionPages(final Long adminId) {
        return this.baseMapper.listPermissionPages(adminId);
    }

    private List<Page> getPages(final Admin admin) {
        final List<SysPage> allPages = this.listSysPages();
        final Map<Long, SysPage> allPageMap = toMap(allPages);

        // 非顶级集合
        final List<Page> nonRootPageList = new ArrayList<>();
        // 顶级集合
        final List<Page> rootPageList = new ArrayList<>();
        final List<Page> pageVoList = this.listPermissionPages(admin.getId());

        final Map<Long, Page> permission = new HashMap<>((int) (0.75 / pageVoList.size()));
        for (final Page pageVo : pageVoList) {
            permission.put(pageVo.getId(), pageVo);
        }

        for (final SysPage sysPage : allPages) {
            if (!admin.getSysRole().getSuperAdmin() && !permission.containsKey(sysPage.getId())) {
                continue;
            }
            if (0 == sysPage.getParentId()) {
                final Page rootPageVo = CommonTool.toVo(sysPage, Page.class);
                final Page permissionPageVo = permission.get(rootPageVo.getId());
                if (null != permissionPageVo) {
                    rootPageVo.setCanInsert(permissionPageVo.getCanInsert());
                    rootPageVo.setCanDelete(permissionPageVo.getCanDelete());
                    rootPageVo.setCanUpdate(permissionPageVo.getCanUpdate());
                    rootPageVo.setCanSelect(permissionPageVo.getCanSelect());
                }
                rootPageList.add(rootPageVo);
            } else {
                nonRootPageList.add(CommonTool.toVo(sysPage, Page.class));
            }
            if (sysPage.getIsDefault()) {
                admin.setDefaultPage(sysPage.getUrl());
            }
        }

        for (final Page pageVo : nonRootPageList) {
            final SysPage sysPageParent = allPageMap.get(pageVo.getParentId());
            if (null != sysPageParent) {
                final Optional<Page> first = rootPageList.stream().filter(p -> p.getId().equals(sysPageParent.getId())).findFirst();
                if (!first.isPresent()) {
                    final Page rootPageVo = CommonTool.toVo(sysPageParent, Page.class);
                    final Page permissionPageVo = permission.get(rootPageVo.getId());
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
            rootPageList.forEach(rootPage -> getChild(admin, permission, rootPage, nonRootPageList, map));
            filter(rootPageList);
            return rootPageList;
        }
        return null;
    }

    private Map<Long, SysPage> toMap(final List<SysPage> sysPageList) {
        final Map<Long, SysPage> result = new HashMap<>((int) (sysPageList.size() / 0.75));

        for (final SysPage sysPage : sysPageList) {
            result.put(sysPage.getId(), sysPage);
        }
        return result;
    }

    private void filter(final List<Page> pageList) {
        final Iterator<Page> iterator = pageList.iterator();
        while (iterator.hasNext()) {
            final Page page = iterator.next();
            if (ObjectUtils.isEmpty(page.getUrl()) && (page.getChildren() == null || page.getChildren().size() < 1)) {
                iterator.remove();
            } else {
                filter(page.getChildren());
            }
        }
    }

    private void getChild(final Admin admin,
                          final Map<Long, Page> permission,
                          final Page parentPage,
                          final List<Page> childrenPageList,
                          final Set<Long> set) {
        final List<Page> childList = Lists.newArrayList();
        childrenPageList.stream().//
                filter(p -> !set.contains(p.getId())). // 判断是否已循环过当前对象
                filter(p -> p.getParentId().equals(parentPage.getId())). // 判断是否父子关系
                filter(p -> set.size() <= childrenPageList.size()).// set集合大小不能超过childrenPageList的大小
                forEach(p -> {
            if (admin.getSysRole().getSuperAdmin() || ObjectUtils.isEmpty(p.getUrl()) || permission.containsKey(p.getId())) {
                // 放入set, 递归循环时可以跳过这个页面，提高循环效率
                set.add(p.getId());
                // 递归获取当前类目的子类目
                getChild(admin, permission, p, childrenPageList, set);

                if (permission.containsKey(p.getId())) {
                    final Page page = permission.get(p.getId());
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