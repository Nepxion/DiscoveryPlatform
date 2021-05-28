package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.response.Result;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.tool.CommonTool;

@Controller
@RequestMapping(PageController.PREFIX)
public class PageController {
    public static final String PREFIX = "page";

    @Autowired
    private PageService pageService;


    @GetMapping("list")
    public String list() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @RequestMapping("add")
    public String add(Model model) throws Exception {
        model.addAttribute("pages", this.pageService.listEmptyUrlPages());
        return String.format("%s/%s", PREFIX, "add");
    }

    @RequestMapping("edit")
    public String edit(Model model,
                       @RequestParam(value = "id") Long id) throws Exception {
        model.addAttribute("page", this.pageService.getById(id));
        model.addAttribute("pages", this.pageService.listEmptyUrlPages());
        return String.format("%s/%s", PREFIX, "edit");
    }

    @PostMapping("do-list")
    @ResponseBody
    public Result<List<PageVo>> doList(@RequestParam(value = "page") Integer pageNum,
                                       @RequestParam(value = "limit") Integer pageSize,
                                       @RequestParam(value = "name", required = false) String name) throws Exception {
        IPage<PageVo> page = this.pageService.list(name, pageNum, pageSize);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("do-add")
    @ResponseBody
    public Result<?> doAdd(SysPageDto sysPage) throws Exception {
        if (sysPage.getIsDefault() == null) {
            sysPage.setIsDefault(false);
        }
        if (sysPage.getIsMenu() == null) {
            sysPage.setIsMenu(false);
        }
        if (sysPage.getIsBlank() == null) {
            sysPage.setIsBlank(false);
        }
        Long order = this.pageService.getMaxOrder(sysPage.getParentId());
        sysPage.setOrder(order + 1);
        this.pageService.insert(sysPage);
        return Result.ok();
    }

    @PostMapping("do-edit")
    @ResponseBody
    public Result<?> doEdit(SysPageDto sysPage) {
        SysPageDto dbSysPage = this.pageService.getById(sysPage.getId());
        if (dbSysPage != null) {
            if (sysPage.getIsDefault() == null) {
                sysPage.setIsDefault(false);
            }
            if (sysPage.getIsMenu() == null) {
                sysPage.setIsMenu(false);
            }
            if (sysPage.getIsBlank() == null) {
                sysPage.setIsBlank(false);
            }
            this.pageService.updateById(sysPage);
        }
        return Result.ok();
    }

    @PostMapping("do-delete")
    @ResponseBody
    public Result<?> doDelete(@RequestParam(value = "ids") String ids) {
        List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.pageService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}