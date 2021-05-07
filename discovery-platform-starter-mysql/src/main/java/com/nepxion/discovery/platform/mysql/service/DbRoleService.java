package com.nepxion.discovery.platform.mysql.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nepxion.discovery.platform.mysql.mapper.DbRoleMapper;
import com.nepxion.discovery.platform.server.ui.entity.dto.SysRole;
import com.nepxion.discovery.platform.server.ui.interfaces.RoleService;
import com.nepxion.discovery.platform.server.ui.tool.anno.TranRead;
import com.nepxion.discovery.platform.server.ui.tool.anno.TranSave;
import com.nepxion.discovery.platform.server.ui.tool.exception.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Set;

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
public class DbRoleService extends ServiceImpl<DbRoleMapper, SysRole> implements RoleService {
    @TranRead
    @Override
    public List<SysRole> listOrderByName() {
        final QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByAsc(SysRole::getName);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public List<SysRole> getNotSuperAdmin() {
        final QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(SysRole::getSuperAdmin, false)
                .orderByAsc(SysRole::getRowCreateTime);
        return this.list(queryWrapper);
    }

    @TranRead
    @Override
    public IPage<SysRole> list(String name, Integer pageNum, Integer pageSize) {
        final QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = queryWrapper.lambda().orderByAsc(SysRole::getName);
        if (!ObjectUtils.isEmpty(name)) {
            sysRoleLambdaQueryWrapper.like(SysRole::getName, name);
        }
        return this.page(new Page<>(), queryWrapper);
    }

    @TranSave
    @Override
    public void insert(String name, Boolean superAdmin, String remark) {
        SysRole sysRole = this.getByUserName(name);
        if (null != sysRole) {
            throw new BusinessException(String.format("角色名[%s]已存在", name));
        }
        sysRole = new SysRole();
        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setRemark(remark);
        this.save(sysRole);
    }

    @TranSave
    @Override
    public void update(final Long id,
                       final String name,
                       final Boolean superAdmin,
                       final String remark) {
        if (StringUtils.isEmpty(name) &&
                null == superAdmin &&
                StringUtils.isEmpty(remark)) {
            throw new BusinessException("请输入要更新的内容");
        }

        final SysRole sysRole = this.getById(id);
        if (null == sysRole) {
            throw new BusinessException(String.format("角色[%s]不存在", name));
        }

        sysRole.setName(name);
        sysRole.setSuperAdmin(superAdmin);
        sysRole.setRemark(remark);
        this.updateById(sysRole);
    }

    @Override
    public SysRole getById(final Long sysRoleId) {
        return super.getById(sysRoleId);
    }

    @Override
    public boolean removeByIds(final Set<Long> idSet) {
        return super.removeByIds(idSet);
    }

    private SysRole getByUserName(final String name) {
        final QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRole::getName, name);
        return this.getOne(queryWrapper);
    }
}