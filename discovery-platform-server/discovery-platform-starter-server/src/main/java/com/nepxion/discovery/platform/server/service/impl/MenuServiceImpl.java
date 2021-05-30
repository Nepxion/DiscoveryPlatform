package com.nepxion.discovery.platform.server.service.impl;

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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nepxion.discovery.platform.server.annotation.TransactionReader;
import com.nepxion.discovery.platform.server.annotation.TransactionWriter;
import com.nepxion.discovery.platform.server.entity.dto.SysMenuDto;
import com.nepxion.discovery.platform.server.entity.po.MenuPo;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.MenuVo;
import com.nepxion.discovery.platform.server.mapper.MenuMapper;
import com.nepxion.discovery.platform.server.service.MenuService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

public class MenuServiceImpl extends ServiceImpl<MenuMapper, SysMenuDto> implements MenuService {
    @TransactionReader
    @Override
    public List<SysMenuDto> list() {
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
    public List<SysMenuDto> listEmptyUrlMenus() {
        QueryWrapper<SysMenuDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysMenuDto::getUrl, StringUtils.EMPTY)
                .orderByAsc(SysMenuDto::getName);
        return list(queryWrapper);
    }

    @SuppressWarnings("unchecked")
    @TransactionReader
    @Override
    public List<SysMenuDto> listNotEmptyUrlMenus() {
        QueryWrapper<SysMenuDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .ne(SysMenuDto::getUrl, StringUtils.EMPTY)
                .orderByAsc(SysMenuDto::getName);
        return list(queryWrapper);
    }

    @TransactionReader
    @Override
    public IPage<MenuVo> list(String name, Integer pageNum, Integer pageSize) {
        return baseMapper.list(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), name);
    }

    @TransactionReader
    @Override
    public Long getMaxOrder(Long parentId) {
        return baseMapper.getMaxOrder(parentId);
    }

    @TransactionWriter
    @Override
    public boolean insert(MenuPo menuPo) throws Exception {
        if (menuPo == null) {
            return false;
        }
        SysMenuDto sysMenuDto = new SysMenuDto();
        BeanUtils.copyProperties(sysMenuDto, menuPo);
        return super.save(sysMenuDto);
    }

    @TransactionReader
    @Override
    public SysMenuDto getById(Long id) {
        return super.getById(id);
    }

    @TransactionWriter
    @Override
    public boolean updateById(MenuPo menuPo) throws Exception {
        if (menuPo == null) {
            return false;
        }
        SysMenuDto sysMenuDto = new SysMenuDto();
        BeanUtils.copyProperties(sysMenuDto, menuPo);
        return super.updateById(sysMenuDto);
    }

    @TransactionWriter
    @Override
    public boolean removeByIds(Set<Long> idList) {
        return super.removeByIds(idList);
    }

    @SuppressWarnings("unchecked")
    private List<SysMenuDto> listSysMenus() {
        QueryWrapper<SysMenuDto> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysMenuDto::getShowFlag, 1)
                .orderByAsc(SysMenuDto::getParentId, SysMenuDto::getOrder);
        return list(queryWrapper);
    }

    private List<MenuVo> listPermissionMenus(Long adminId) {
        return baseMapper.listPermissionMenus(adminId);
    }

    private List<MenuVo> getPages(AdminVo adminVo) {
        List<SysMenuDto> allMenus = listSysMenus();
        Map<Long, SysMenuDto> allMenuMap = toMap(allMenus);

        // 非顶级集合
        List<MenuVo> nonRootPageList = new ArrayList<>();
        // 顶级集合
        List<MenuVo> rootPageList = new ArrayList<>();
        List<MenuVo> pageVoList = listPermissionMenus(adminVo.getId());

        Map<Long, MenuVo> permission = new HashMap<>((int) (0.75 / pageVoList.size()));
        for (MenuVo menuVo : pageVoList) {
            permission.put(menuVo.getId(), menuVo);
        }

        for (SysMenuDto sysMenuDto : allMenus) {
            if (!adminVo.getSysRole().getSuperAdmin() && !permission.containsKey(sysMenuDto.getId())) {
                continue;
            }
            if (sysMenuDto.getParentId() == 0) {
                MenuVo rootMenuVo = CommonTool.toVo(sysMenuDto, MenuVo.class);
                MenuVo permissionPageVo = permission.get(rootMenuVo.getId());
                if (permissionPageVo != null) {
                    rootMenuVo.setCanInsert(permissionPageVo.getCanInsert());
                    rootMenuVo.setCanDelete(permissionPageVo.getCanDelete());
                    rootMenuVo.setCanUpdate(permissionPageVo.getCanUpdate());
                    rootMenuVo.setCanSelect(permissionPageVo.getCanSelect());
                }
                rootPageList.add(rootMenuVo);
            } else {
                nonRootPageList.add(CommonTool.toVo(sysMenuDto, MenuVo.class));
            }
            if (sysMenuDto.getDefaultFlag()) {
                adminVo.setDefaultPage(sysMenuDto.getUrl());
            }
        }

        for (MenuVo menuVo : nonRootPageList) {
            SysMenuDto sysMenuDto = allMenuMap.get(menuVo.getParentId());
            if (sysMenuDto != null) {
                Optional<MenuVo> first = rootPageList.stream().filter(p -> p.getId().equals(sysMenuDto.getId())).findFirst();
                if (!first.isPresent()) {
                    MenuVo rootMenuVo = CommonTool.toVo(sysMenuDto, MenuVo.class);
                    MenuVo permissionMenuVo = permission.get(rootMenuVo.getId());
                    if (permissionMenuVo != null) {
                        rootMenuVo.setCanInsert(permissionMenuVo.getCanInsert());
                        rootMenuVo.setCanDelete(permissionMenuVo.getCanDelete());
                        rootMenuVo.setCanUpdate(permissionMenuVo.getCanUpdate());
                        rootMenuVo.setCanSelect(permissionMenuVo.getCanSelect());
                    }
                    rootPageList.add(rootMenuVo);
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

    private Map<Long, SysMenuDto> toMap(List<SysMenuDto> sysMenuDtoList) {
        Map<Long, SysMenuDto> result = new HashMap<>();
        for (SysMenuDto sysMenuDto : sysMenuDtoList) {
            result.put(sysMenuDto.getId(), sysMenuDto);
        }
        return result;
    }

    private void filter(List<MenuVo> menuVoList) {
        Iterator<MenuVo> iterator = menuVoList.iterator();
        while (iterator.hasNext()) {
            MenuVo menuVo = iterator.next();
            if (StringUtils.isEmpty(menuVo.getUrl()) && (menuVo.getChildren() == null || menuVo.getChildren().size() < 1)) {
                iterator.remove();
            } else {
                filter(menuVo.getChildren());
            }
        }
    }

    private void getChild(AdminVo adminVo, Map<Long, MenuVo> permission, MenuVo parentMenu, List<MenuVo> childrenMenuList, Set<Long> set) {
        List<MenuVo> childList = Lists.newArrayList();
        childrenMenuList.stream().//
                filter(m -> !set.contains(m.getId())). // 判断是否已循环过当前对象
                filter(m -> m.getParentId().equals(parentMenu.getId())). // 判断是否父子关系
                filter(m -> set.size() <= childrenMenuList.size()).// set集合大小不能超过childrenPageList的大小
                forEach(m -> {
            if (adminVo.getSysRole().getSuperAdmin() || StringUtils.isEmpty(m.getUrl()) || permission.containsKey(m.getId())) {
                // 放入set, 递归循环时可以跳过这个页面，提高循环效率
                set.add(m.getId());
                // 递归获取当前类目的子类目
                getChild(adminVo, permission, m, childrenMenuList, set);

                if (permission.containsKey(m.getId())) {
                    MenuVo menuVo = permission.get(m.getId());
                    m.setCanInsert(menuVo.getCanInsert());
                    m.setCanDelete(menuVo.getCanDelete());
                    m.setCanUpdate(menuVo.getCanUpdate());
                    m.setCanSelect(menuVo.getCanSelect());
                }
                childList.add(m);
            }
        });
        parentMenu.setChildren(childList);
    }
}