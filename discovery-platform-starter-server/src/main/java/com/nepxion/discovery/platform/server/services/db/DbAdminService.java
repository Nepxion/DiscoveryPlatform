package com.nepxion.discovery.platform.server.services.db;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nepxion.discovery.platform.server.common.Tool;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.enums.Mode;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;
import com.nepxion.discovery.platform.server.ineterfaces.RoleService;
import com.nepxion.discovery.platform.tool.anno.TranRead;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import com.nepxion.discovery.platform.tool.db.CrudPage;
import com.nepxion.discovery.platform.tool.db.CrudService;
import com.nepxion.discovery.platform.tool.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

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

public class DbAdminService implements AdminService {
    @Autowired
    private CrudService crudService;
    @Autowired
    private RoleService roleService;

    @TranRead
    @Override
    public boolean authenticate(final String username,
                                final String password) throws Exception {
        if (ObjectUtils.isEmpty(username) || ObjectUtils.isEmpty(password)) {
            return false;
        }
        final SysAdmin sysAdmin = this.getByUserName(username);
        if (null == sysAdmin) {
            return false;
        }

        return Tool.hash(password).equals(sysAdmin.getPassword());
    }

    @TranRead
    @Override
    public Admin getAdminByUserName(final String username) throws Exception {
        final Admin admin = this.crudService.selectOne(Admin.class, "SELECT * FROM `sys_admin` WHERE `username`=?", username);
        if (null != admin) {
            admin.setSysRole(this.roleService.getById(admin.getSysRoleId()));
        }
        return admin;
    }

    @TranRead
    @Override
    public SysAdmin getByUserName(final String username) throws Exception {
        return this.crudService.selectOne(SysAdmin.class, "SELECT * FROM `sys_admin` WHERE `username`=?", username);
    }

    @TranRead
    @Override
    public SysAdmin getById(final Long id) throws Exception {
        return this.crudService.selectOne(SysAdmin.class, "SELECT * FROM `sys_admin` WHERE `id`=?", id);
    }

    @TranSave
    @Override
    public boolean resetPassword(final Long id) throws Exception {
        this.changePassword(id, PlatformConstant.DEFAULT_ADMIN_PASSWORD);
        return true;
    }

    @TranSave
    @Override
    public boolean changePassword(final Long id,
                                  final String oldPassword,
                                  final String newPassword) throws Exception {
        final SysAdmin sysAdmin = this.getById(id);
        if (null == sysAdmin) {
            return false;
        }

        if (!sysAdmin.getPassword().equals(Tool.hash(oldPassword))) {
            throw new BusinessException("密码不匹配, 修改密码失败");
        }

        this.changePassword(id, newPassword);
        return true;
    }

    @TranSave
    @Override
    public void insert(final Mode mode,
                       final Long roleId,
                       final String username,
                       final String password,
                       final String name,
                       final String phoneNumber,
                       final String email,
                       final String remark) throws Exception {
        final SysAdmin sysAdmin = this.getByUserName(username);
        if (null != sysAdmin) {
            throw new BusinessException(String.format("用户名[%s]已存在", username));
        }
        this.crudService.execute("INSERT INTO `sys_admin`(`login_mode`, `sys_role_id`,`username`,`password`,`name`,`phone_number`,`email`,`remark`) VALUES(?,?,?,?,?,?,?,?)",
                mode.getCode(),
                roleId,
                username,
                Tool.hash(password),
                name,
                phoneNumber,
                email,
                remark);
    }

    @TranSave
    @Override
    public void update(final Long id,
                       Long roleId,
                       String username,
                       String name,
                       String phoneNumber,
                       String email,
                       String remark) throws Exception {

        final SysAdmin sysAdmin = this.getById(id);

        if (null == sysAdmin) {
            return;
        }

        if (null == roleId) {
            roleId = sysAdmin.getSysRoleId();
        }
        if (null == username) {
            username = sysAdmin.getUsername();
        }
        if (null == name) {
            name = sysAdmin.getName();
        }
        if (null == phoneNumber) {
            phoneNumber = sysAdmin.getPhoneNumber();
        }
        if (null == email) {
            email = sysAdmin.getEmail();
        }
        if (null == remark) {
            remark = sysAdmin.getRemark();
        }

        this.crudService.execute("UPDATE `sys_admin` SET `sys_role_id`=?,`username`=?,`name`=?,`phone_number`=?,`email`=?,`remark`=? WHERE `id`=?",
                roleId,
                username,
                name,
                phoneNumber,
                email,
                remark,
                id);
    }

    @Override
    @TranSave
    public void removeById(final List<Long> idList) throws Exception {
        for (final Long id : idList) {
            this.crudService.execute("DELETE FROM `sys_admin` WHERE `id`=?", id);
        }
    }

    @TranRead
    @Override
    public IPage<Admin> list(final Mode mode,
                             final String name,
                             final Integer pageNum,
                             final Integer pageSize) throws Exception {
        final StringBuilder sql = new StringBuilder();
        sql.append("SELECT `admin`.`id`  AS `id`," +
                " `admin`.`login_mode`," +
                " `admin`.`username`," +
                " `admin`.`name`," +
                " `admin`.`phone_number`," +
                " `admin`.`email`," +
                " `admin`.`remark`," +
                " `admin`.`row_create_time`," +
                " `role`.`id`   AS `sys_role_id`," +
                " `role`.`name` AS `role_name`" +
                " FROM `sys_admin` `admin`" +
                " LEFT OUTER JOIN `sys_role` `role` ON `admin`.`sys_role_id` = `role`.`id` WHERE 1=1");

        final List<Object> params = new ArrayList<>();
        if (!ObjectUtils.isEmpty(mode)) {
            sql.append(" AND `admin`.`login_mode` = ?");
            params.add(mode.getCode());
        }
        if (!ObjectUtils.isEmpty(name)) {
            sql.append(" AND `admin`.`name` LIKE ?");
            params.add("%".concat(name).concat("%"));
        }

        sql.append(" ORDER BY `admin`.`username` ASC");
        final IPage<Admin> result = new Page<>(pageNum, pageSize);
        final CrudPage<Admin> crudPage = this.crudService.page(Admin.class, pageNum, pageSize, sql.toString(), params.toArray(new Object[]{}));
        result.setRecords(crudPage.getRecords());
        result.setTotal(crudPage.getTotal());
        return result;
    }

    @TranRead
    @Override
    public List<Admin> search(final String keyword,
                              final int offset,
                              final int limit) throws Exception {
        return this.crudService.select(Admin.class, String.format("SELECT * FROM `sys_admin` WHERE `name` LIKE '%%%s%%' LIMIT %s, %s", keyword, offset, limit));
    }

    @TranRead
    @Override
    public List<SysAdmin> getByRoleId(final Long roleId) throws Exception {
        return this.crudService.select(SysAdmin.class, "SELECT * FROM `sys_admin` WHERE `sys_role_id`=?", roleId);
    }

    private void changePassword(final Long id,
                                final String newPassword) throws Exception {
        this.crudService.execute("UPDATE `sys_admin` SET `password`=? WHERE `id`=?",
                Tool.hash(newPassword),
                id);
    }
}
