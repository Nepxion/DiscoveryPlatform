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

import com.nepxion.discovery.platform.server.tool.JwtTool;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nepxion.discovery.platform.server.entity.po.AdminPo;
import com.nepxion.discovery.platform.server.entity.po.ChangePasswordPo;
import com.nepxion.discovery.platform.server.entity.po.LoginPo;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api("登录、登出、修改密码等接口")
@RestController
public class IndexController {
    @Autowired
    private AdminService adminService;

    @ApiOperation("执行管理员登录系统")
    @PostMapping("do-login")
    public Result<?> doLogin(LoginPo loginPo) {
        try {
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(loginPo.getUsername(), loginPo.getPassword());
            usernamePasswordToken.setRememberMe(loginPo.getRemember());
            subject.login(usernamePasswordToken);

            AdminVo adminVo = (AdminVo) subject.getPrincipal();
            String jwtToken = JwtTool.generateToken(adminVo);
            return Result.ok(jwtToken);
        } catch (UnknownAccountException e) {
            return Result.error("无效的用户名或密码");
        } catch (Exception e) {
            return Result.error(String.format("账号或密码错误. 原因:%s", ExceptionTool.getRootCauseMessage(e)));
        }
    }

    @ApiOperation("修改管理员的登录密码")
    @PostMapping("do-change-password")
    public Result<?> doChangePassword(AdminVo adminVo, ChangePasswordPo changePasswordPo) throws Exception {
        adminService.changePassword(adminVo.getId(), CommonTool.hash(changePasswordPo.getOldPassword()), CommonTool.hash(changePasswordPo.getPassword()));
        return Result.ok();
    }

    @ApiOperation("更新管理员的信息")
    @PostMapping("do-update-info")
    public Result<?> doUpdateInfo(AdminVo adminVo, AdminPo adminPo) throws Exception {
        adminPo.setId(adminVo.getId());
        adminService.update(adminPo);
        return Result.ok();
    }

    @ApiOperation("执行管理员退出系统逻辑")
    @PostMapping("do-quit")
    public Result<?> doQuit() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return Result.ok();
    }
}