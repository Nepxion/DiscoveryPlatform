package com.nepxion.discovery.platform.server.services.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.platform.server.entity.dto.SysPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPermission;
import com.nepxion.discovery.platform.server.entity.vo.Permission;
import com.nepxion.discovery.platform.server.ineterfaces.PermissionService;
import com.nepxion.discovery.platform.tool.anno.TranRead;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import com.nepxion.discovery.platform.tool.db.CrudPage;
import com.nepxion.discovery.platform.tool.db.CrudService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
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

public class DbPermissionService implements PermissionService {
    @Autowired
    private CrudService crudService;

    @TranRead
    @Override
    public List<SysPage> listPermissionPagesByRoleId(final Long sysRoleId) throws Exception {
        return this.crudService.select(SysPage.class, "SELECT" +
                " `page`.*" +
                " FROM `sys_page` `page`" +
                " LEFT OUTER JOIN `sys_permission` `p` ON `page`.`id`=`p`.`sys_page_id`" +
                " WHERE `p`.`sys_role_id`=?", sysRoleId);
    }

    @TranRead
    @Override
    public IPage<Permission> list(final Integer pageNum,
                                  final Integer pageSize,
                                  final Long sysRoleId,
                                  final Long sysPageId) throws Exception {
        final StringBuilder sql = new StringBuilder();
        final List<Object> objectList = new ArrayList<>();
        sql.append("SELECT" +
                " `p`.`id`," +
                " `page`.`id` AS `page_id`," +
                " `page`.`name` AS `page_name`," +
                " `r`.`id` AS `role_id`," +
                " `r`.`name` AS `role_name`," +
                " `p`.`can_insert`," +
                " `p`.`can_delete`," +
                " `p`.`can_update`," +
                " `p`.`can_select`" +
                " FROM `sys_permission` `p`" +
                " LEFT OUTER JOIN `sys_page` `page` ON `page`.`id` = `p`.`sys_page_id`" +
                " LEFT OUTER JOIN `sys_role` `r` ON `p`.`sys_role_id` = `r`.`id`" +
                " WHERE 1=1");
        if (null != sysRoleId) {
            sql.append("AND `r`.`id`=?");
            objectList.add(sysRoleId);
        }
        if (null != sysPageId) {
            sql.append("AND `page`.`id`=?");
            objectList.add(sysPageId);
        }

        final CrudPage<Permission> crudPage = this.crudService.page(Permission.class, pageNum, pageSize, sql.toString(), objectList.toArray());
        final IPage<Permission> result = new Page<>(pageNum, pageSize);
        result.setRecords(crudPage.getRecords());
        result.setTotal(crudPage.getTotal());
        return result;
    }

    @TranSave
    @Override
    public void insert(final SysPermission sysPermission) throws Exception {
        this.crudService.execute("INSERT INTO `sys_permission`(`sys_role_id`,`sys_page_id`,`can_insert`,`can_delete`,`can_update`,`can_select`) VALUES(?,?,?,?,?,?)",
                sysPermission.getSysRoleId(),
                sysPermission.getSysPageId(),
                sysPermission.getCanInsert(),
                sysPermission.getCanDelete(),
                sysPermission.getCanUpdate(),
                sysPermission.getCanSelect());
    }

    @TranRead
    @Override
    public SysPermission getById(final Long id) throws Exception {
        return this.crudService.selectOne(SysPermission.class, "SELECT * FROM `sys_permission` WHERE `id`=?", id);
    }

    @TranSave
    @Override
    public void updateById(final SysPermission sysPermission) throws Exception {
        this.crudService.execute("UPDATE `sys_permission` SET `sys_role_id`=?,`sys_page_id`=?,`can_insert`=?,`can_delete`=?,`can_update`=?,`can_select`=? WHERE `id`=?",
                sysPermission.getSysRoleId(),
                sysPermission.getSysPageId(),
                sysPermission.getCanInsert(),
                sysPermission.getCanDelete(),
                sysPermission.getCanUpdate(),
                sysPermission.getCanSelect(),
                sysPermission.getId());
    }

    @TranSave
    @Override
    public void removeByIds(List<Long> idsList) throws Exception {
        if (null == idsList || idsList.isEmpty()) {
            return;
        }
        final StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM `sys_permission` WHERE `id` IN (");
        for (final Long id : idsList) {
            sql.append(id.toString().concat(","));
        }
        sql.delete(sql.length() - 1, sql.length());
        sql.append(")");

        this.crudService.execute(sql.toString());
    }
}
