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

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.service.AdminService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

@Controller
public class IndexPageController {
    @Autowired
    private AdminService adminService;

    @GetMapping(value = { PlatformConstant.PLATFORM })
    public String login(Model model) {
        model.addAttribute("version", CommonTool.getVersion());
        model.addAttribute("year", Calendar.getInstance().get(Calendar.YEAR));
        return "login";
    }

    @RequestMapping(value = "index")
    public String index(Model model, AdminVo adminVo) {
        model.addAttribute("version", CommonTool.getVersion());
        model.addAttribute("admin", adminVo);
        return "index";
    }

    @GetMapping("info")
    public String info(Model model, AdminVo adminVo) {
        model.addAttribute("admin", adminService.getById(adminVo.getId()));
        return "info";
    }

    @GetMapping("password")
    public String password() {
        return "password";
    }
}