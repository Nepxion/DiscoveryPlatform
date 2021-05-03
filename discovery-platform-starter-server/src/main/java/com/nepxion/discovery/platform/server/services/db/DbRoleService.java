package com.nepxion.discovery.platform.server.services.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.platform.server.entity.dto.SysRole;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;
import com.nepxion.discovery.platform.tool.anno.TranRead;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import com.nepxion.discovery.platform.tool.db.CrudPage;
import com.nepxion.discovery.platform.tool.db.CrudService;
import com.nepxion.discovery.platform.tool.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
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

public class DbRoleService implements RoleService {
    @Autowired
    private CrudService crudService;

    @TranRead
    @Override
    public List<SysRole> listOrderByName() throws Exception {
        return crudService.select(SysRole.class, "SELECT * FROM `sys_role` ORDER BY `name` ASC");
    }

    @TranRead
    @Override
    public List<SysRole> getNotSuperAdmin() throws Exception {
        return this.crudService.select(SysRole.class, "SELECT * FROM `sys_role` WHERE `super_admin`=false ORDER BY `row_create_time` ASC");
    }

    @TranRead
    @Override
    public IPage<SysRole> list(String name, Integer pageNum, Integer pageSize) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM `sys_role` WHERE 1=1");
        if (!ObjectUtils.isEmpty(name)) {
            sql.append(" AND `name` LIKE ?");
        }
        sql.append(" ORDER BY `name` ASC");
        final IPage<SysRole> result = new Page<>(pageNum, pageSize);
        final CrudPage<SysRole> crudPage = crudService.page(SysRole.class, pageNum, pageSize, sql.toString(), ObjectUtils.isEmpty(name) ? null : "%".concat(name).concat("%"));
        result.setRecords(crudPage.getRecords());
        result.setTotal(crudPage.getTotal());
        return result;
    }

    @TranSave
    @Override
    public void insert(String name, Boolean superAdmin, String remark) throws Exception {
        final SysRole sysRole = this.getByUserName(name);
        if (null != sysRole) {
            throw new BusinessException(String.format("角色名[%s]已存在", name));
        }
        this.crudService.execute("INSERT INTO `sys_role`(`name`, `super_admin`, `remark`) VALUES (?,?,?)",
                name,
                superAdmin,
                remark);
    }

    @TranSave
    @Override
    public void update(final Long id,
                       final String name,
                       final Boolean superAdmin,
                       final String remark) throws Exception {
        final SysRole sysRole = this.getById(id);
        if (null == sysRole) {
            throw new BusinessException(String.format("角色[%s]不存在", name));
        }
        this.crudService.execute("UPDATE `sys_role` SET `name`=?, `super_admin`=?, `remark`=? WHERE `id`=?",
                name,
                superAdmin,
                remark,
                id);
    }

    @TranRead
    @Override
    public SysRole getById(final Long sysRoleId) throws Exception {
        if (null == sysRoleId) {
            return null;
        }
        return crudService.selectOne(SysRole.class, "SELECT * FROM `sys_role` WHERE `id`=?", sysRoleId);
    }

    @TranSave
    @Override
    public void removeById(final Collection<Long> ids) throws Exception {
        for (final Long id : ids) {
            this.crudService.execute("DELETE FROM `sys_role` WHERE `id`=?", id);
        }
    }

    private SysRole getByUserName(final String name) throws Exception {
        return crudService.selectOne(SysRole.class, "SELECT * FROM `sys_role` WHERE `name`=?", name);
    }

}
