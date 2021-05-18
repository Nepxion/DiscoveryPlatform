package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.adapter.LoginAdapter;
import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.dto.SysAdminDto;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.exception.BusinessException;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.service.RoleService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(AdminController.PREFIX)
public class AdminController {
    public static final String PREFIX = "admin";

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private LoginAdapter loginAdapter;

    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @GetMapping("toadd")
    public String toAdd(final Model model) throws Exception {
        model.addAttribute("roles", this.roleService.listOrderByName());

        if (this.loginAdapter.getLoginMode() == LoginMode.MYSQL) {
            return String.format("%s/%s", PREFIX, "add");
        }
        if (this.loginAdapter.getLoginMode() == LoginMode.LDAP) {
            return String.format("%s/%s", PREFIX, "addldap");
        }
        throw new BusinessException(String.format("暂不支持登录模式[%s]", this.loginAdapter.getLoginMode()));
    }

    @GetMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(name = "id") final Long id) throws Exception {
        model.addAttribute("admin", this.adminService.getById(id));
        model.addAttribute("roles", this.roleService.listOrderByName());
        model.addAttribute("loginMode", this.loginAdapter.getLoginMode());
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<AdminVo>> list(@RequestParam(value = "name", required = false) final String name,
                                      @RequestParam(value = "page") final Integer pageNum,
                                      @RequestParam(value = "limit") final Integer pageSize) throws Exception {
        final IPage<AdminVo> adminPage = this.adminService.list(this.loginAdapter.getLoginMode(), name, pageNum, pageSize);
        return Result.ok(adminPage.getRecords(), adminPage.getTotal());
    }

    @PostMapping("repwd")
    @ResponseBody
    public Result<?> repwd(@RequestParam(value = "id") final Long id) throws Exception {
        final SysAdminDto sysAdmin = this.adminService.getById(id);
        if (null == sysAdmin) {
            return Result.error(String.format("用户[id=%s]不存在", id));
        }
        if (this.adminService.changePassword(id,
                sysAdmin.getPassword(),
                CommonTool.hash(PlatformConstant.DEFAULT_ADMIN_PASSWORD))) {
            return Result.ok();
        } else {
            return Result.error("密码修改失败");
        }
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(@RequestParam(value = "roleId") final Long roleId,
                         @RequestParam(value = "username") final String username,
                         @RequestParam(value = "password", defaultValue = "") final String password,
                         @RequestParam(value = "name") final String name,
                         @RequestParam(value = "phoneNumber") final String phoneNumber,
                         @RequestParam(value = "email") final String email,
                         @RequestParam(value = "remark") final String remark) throws Exception {
        this.adminService.insert(this.loginAdapter.getLoginMode(), roleId, username, password, name, phoneNumber, email, remark);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(@RequestParam(value = "id") final Long id,
                          @RequestParam(value = "roleId") final Long roleId,
                          @RequestParam(value = "username") final String username,
                          @RequestParam(value = "name") final String name,
                          @RequestParam(value = "phoneNumber") final String phoneNumber,
                          @RequestParam(value = "email") final String email,
                          @RequestParam(value = "remark") final String remark) throws Exception {
        this.adminService.update(id, roleId, username, name, phoneNumber, email, remark);
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) throws Exception {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.adminService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }

    @PostMapping("search")
    @ResponseBody
    public Result<List<AdminVo>> search(@RequestParam(value = "keyword", defaultValue = "") final String keyword) throws Exception {
        if (ObjectUtils.isEmpty(keyword.trim())) {
            return Result.ok();
        }
        return Result.ok(this.adminService.search(keyword, 0, 10));
    }
}