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

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.tool.CommonTool;
import com.nepxion.discovery.platform.server.tool.ExceptionTool;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;

@Controller
public class IndexController {
    @Autowired
    private AdminService adminService;

    @GetMapping(value = {PlatformConstant.PLATFORM})
    public String login(final Model model) {
        model.addAttribute("version", CommonTool.getVersion());
        model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        return "login";
    }

    @RequestMapping(value = "index")
    public String index(final Model model,
                          final AdminVo adminVo) {
        model.addAttribute("version", CommonTool.getVersion());
        model.addAttribute("admin", adminVo);
        return "index";
    }

    @GetMapping("info")
    public String info(final Model model,
                         final AdminVo adminVo) {
        model.addAttribute("admin", this.adminService.getById(adminVo.getId()));
        return "info";
    }

    @GetMapping("password")
    public String password() {
        return "password";
    }

    @PostMapping("do-login")
    @ResponseBody
    public Result<?> doLogin(@RequestParam(name = "username") final String username,
                           @RequestParam(name = "password") final String password,
                           @RequestParam(name = "remember", defaultValue = "false") final Boolean remember) {
        try {
            final Subject subject = SecurityUtils.getSubject();
            final UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
            usernamePasswordToken.setRememberMe(remember);
            subject.login(usernamePasswordToken);
            return Result.ok();
        } catch (final UnknownAccountException e) {
            return Result.error("无效的用户名或密码");
        } catch (final Exception e) {
            return Result.error(String.format("账号或密码错误. 原因:%s", ExceptionTool.getRootCauseMessage(e)));
        }
    }

    @PostMapping("do-change-password")
    @ResponseBody
    public Result<?> doChangePassword(final AdminVo adminVo,
                                    @RequestParam(name = "oldPassword") final String oldPassword,
                                    @RequestParam(name = "password") final String newPassword) throws Exception {
        this.adminService.changePassword(adminVo.getId(), CommonTool.hash(oldPassword), CommonTool.hash(newPassword));
        return Result.ok();
    }

    @PostMapping("do-edit-info")
    @ResponseBody
    public Result<?> doEditInfo(final AdminVo adminVo,
                            @RequestParam(name = "name") final String name,
                            @RequestParam(name = "phoneNumber") final String phoneNumber,
                            @RequestParam(name = "email") final String email,
                            @RequestParam(name = "remark") final String remark) throws Exception {
        this.adminService.update(adminVo.getId(), null, null, name, phoneNumber, email, remark);
        return Result.ok();
    }

    @PostMapping("do-quit")
    @ResponseBody
    public Result<?> doQuit() {
        final Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return Result.ok();
    }
}