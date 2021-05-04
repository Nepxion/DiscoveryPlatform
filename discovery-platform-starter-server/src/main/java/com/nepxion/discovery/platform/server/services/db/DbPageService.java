package com.nepxion.discovery.platform.server.services.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.nepxion.discovery.platform.server.common.Tool;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.entity.vo.Page;
import com.nepxion.discovery.platform.server.ineterfaces.PageService;
import com.nepxion.discovery.platform.tool.anno.TranRead;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import com.nepxion.discovery.platform.tool.db.CrudPage;
import com.nepxion.discovery.platform.tool.db.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

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

public class DbPageService implements PageService {
    @Autowired
    private CrudService crudService;

    @Override
    @TranRead
    public void fillPages(final Admin admin) throws Exception {
        if (null == admin) {
            return;
        }
        admin.setPermissions(getPages(admin));
        if (null != admin.getPermissions()) {
            admin.getPermissions().sort((o1, o2) -> (int) (o1.getOrderNum() - o2.getOrderNum()));
        }
    }

    @Override
    public List<SysPage> listEmptyUrlPages() throws Exception {
        return this.crudService.select(SysPage.class, "SELECT * FROM `sys_page` WHERE IFNULL(`url`, '')='' ORDER BY `name` ASC");
    }

    @Override
    public List<SysPage> listAll() throws Exception {
        return this.crudService.select(SysPage.class, "SELECT * FROM `sys_page`");
    }

    @Override
    public SysPage getById(final Long id) throws Exception {
        return this.crudService.selectOne(SysPage.class, "SELECT * FROM `sys_page` WHERE `id`=?", id);
    }

    @Override
    public IPage<Page> list(final String name,
                            final Integer pageNum,
                            final Integer pageSize) throws Exception {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT" +
                " `p`.`id`," +
                " `p`.`name`," +
                " `p`.`url`," +
                " `p`.`is_menu`," +
                " `p`.`is_default`," +
                " `p`.`is_blank`," +
                " `p`.`icon_class`," +
                " `p`.`parent_id`," +
                " `parent`.`name` AS `parent_name`," +
                " `p`.`order_num`," +
                " `p`.`remark`" +
                " FROM `sys_page` `p`" +
                " LEFT OUTER JOIN `sys_page` `parent` ON `p`.`parent_id` = `parent`.`id` WHERE 1=1");
        if (!ObjectUtils.isEmpty(name)) {
            sql.append(" AND `p`.`name` LIKE ?");
        }
        sql.append(" ORDER BY p.parent_id ASC, p.order_num ASC");

        final IPage<Page> result = new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
        final CrudPage<Page> crudPage = this.crudService.page(Page.class, pageNum, pageSize, sql.toString(), ObjectUtils.isEmpty(name) ? null : "%".concat(name).concat("%"));
        result.setRecords(crudPage.getRecords());
        result.setTotal(crudPage.getTotal());
        return result;
    }

    @Override
    public Long getMaxOrderNum(final Long parentId) throws Exception {
        return this.crudService.executeScalar(Long.class,
                "SELECT IFNULL(MAX(`order_num`), 0) FROM `sys_page` WHERE `parent_id` = ?",
                parentId);
    }

    @Override
    public void insert(final SysPage sysPage) throws Exception {
        this.crudService.execute("INSERT INTO `sys_page`(`name`,`url`,`is_menu`,`is_default`,`is_blank`,`icon_class`,`parent_id`,`order_num`,`remark`) VALUES(?,?,?,?,?,?,?,?,?)",
                sysPage.getName(),
                sysPage.getUrl(),
                sysPage.getIsMenu(),
                sysPage.getIsDefault(),
                sysPage.getIsBlank(),
                sysPage.getIconClass(),
                sysPage.getParentId(),
                sysPage.getOrderNum(),
                sysPage.getRemark());
    }

    @Override
    public void updateById(final SysPage sysPage) throws Exception {
        this.crudService.execute("UPDATE `sys_page` SET `name`=?,`url`=?,`is_menu`=?,`is_default`=?,`is_blank`=?,`icon_class`=?,`parent_id`=?,`order_num`=?,`remark`=? WHERE `id`=?",
                sysPage.getName(),
                sysPage.getUrl(),
                sysPage.getIsMenu(),
                sysPage.getIsDefault(),
                sysPage.getIsBlank(),
                sysPage.getIconClass(),
                sysPage.getParentId(),
                sysPage.getOrderNum(),
                sysPage.getRemark(),
                sysPage.getId());
    }

    @TranSave
    @Override
    public void removeByIds(final List<Long> idsList) throws Exception {
        if (null == idsList || idsList.isEmpty()) {
            return;
        }
        final StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM `sys_page` WHERE `id` IN (");
        for (final Long id : idsList) {
            sql.append(id.toString().concat(","));
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");

        this.crudService.execute(sql.toString());
    }

    private List<SysPage> listSysPages() throws Exception {
        return this.crudService.select(SysPage.class,
                "SELECT * FROM `sys_page` WHERE `is_menu`=? ORDER BY parent_id ASC, order_num ASC",
                1);
    }

    private List<Page> listPermissionPages(final Long adminId) throws Exception {
        return this.crudService.select(Page.class,
                "SELECT" +
                        " `page`.`id`," +
                        " `page`.`name`," +
                        " `page`.`url`," +
                        " `page`.`is_menu`," +
                        " `page`.`is_default`," +
                        " `page`.`icon_class`," +
                        " `page`.`parent_id`," +
                        " `parent`.`name` AS `parent_name`," +
                        " `page`.`order_num`," +
                        " `page`.`remark`," +
                        " `p`.`can_insert`," +
                        " `p`.`can_delete`," +
                        " `p`.`can_update`," +
                        " `p`.`can_select`" +
                        " FROM `sys_page` `page`" +
                        " LEFT OUTER JOIN `sys_permission` `p` ON `page`.`id` = `p`.`sys_page_id`" +
                        " LEFT OUTER JOIN `sys_role` `r` ON `r`.`id` = `p`.`sys_role_id`" +
                        " LEFT OUTER JOIN `sys_admin` `a` ON `a`.`sys_role_id` = `r`.`id`" +
                        " LEFT OUTER JOIN `sys_page` `parent` ON `page`.`parent_id` = `parent`.`id`" +
                        " WHERE `a`.`id` = ?", adminId);
    }

    private List<Page> getPages(final Admin admin) throws Exception {
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
                final Page rootPageVo = Tool.toVo(sysPage, Page.class);
                final Page permissionPageVo = permission.get(rootPageVo.getId());
                if (null != permissionPageVo) {
                    rootPageVo.setCanInsert(permissionPageVo.getCanInsert());
                    rootPageVo.setCanDelete(permissionPageVo.getCanDelete());
                    rootPageVo.setCanUpdate(permissionPageVo.getCanUpdate());
                    rootPageVo.setCanSelect(permissionPageVo.getCanSelect());
                }
                rootPageList.add(rootPageVo);
            } else {
                nonRootPageList.add(Tool.toVo(sysPage, Page.class));
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
                    final Page rootPageVo = Tool.toVo(sysPageParent, Page.class);
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
