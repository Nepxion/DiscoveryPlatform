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
import com.nepxion.discovery.platform.server.entity.dto.SysPageDto;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;
import com.nepxion.discovery.platform.server.service.PageService;
import com.nepxion.discovery.platform.server.tool.common.CommonTool;
import com.nepxion.discovery.platform.server.tool.web.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(PageController.PREFIX)
public class PageController {
    public static final String PREFIX = "page";

    @Autowired
    private PageService pageService;


    @GetMapping("tolist")
    public String toList() {
        return String.format("%s/%s", PREFIX, "list");
    }

    @RequestMapping("toadd")
    public String toAdd(final Model model) throws Exception {
        model.addAttribute("pages", this.pageService.listEmptyUrlPages());
        return "page/add";
    }

    @RequestMapping("toedit")
    public String toEdit(final Model model,
                         @RequestParam(value = "id") final Long id) throws Exception {
        model.addAttribute("page", this.pageService.getById(id));
        model.addAttribute("pages", this.pageService.listEmptyUrlPages());
        return "page/edit";
    }

    @PostMapping("list")
    @ResponseBody
    public Result<List<PageVo>> list(@RequestParam(value = "page") final Integer pageNum,
                                     @RequestParam(value = "limit") final Integer pageSize,
                                     @RequestParam(value = "name", required = false) final String name) throws Exception {
        final IPage<PageVo> page = this.pageService.list(name, pageNum, pageSize);
        return Result.ok(page.getRecords(), page.getTotal());
    }

    @PostMapping("add")
    @ResponseBody
    public Result<?> add(final SysPageDto sysPage) throws Exception {
        if (null == sysPage.getIsDefault()) {
            sysPage.setIsDefault(false);
        }
        if (null == sysPage.getIsMenu()) {
            sysPage.setIsMenu(false);
        }
        if (null == sysPage.getIsBlank()) {
            sysPage.setIsBlank(false);
        }
        final Long order = this.pageService.getMaxOrder(sysPage.getParentId());
        sysPage.setOrder(order + 1);
        this.pageService.insert(sysPage);
        return Result.ok();
    }

    @PostMapping("edit")
    @ResponseBody
    public Result<?> edit(final SysPageDto sysPage) throws Exception {
        final SysPageDto dbSysPage = this.pageService.getById(sysPage.getId());
        if (null != dbSysPage) {
            if (null == sysPage.getIsDefault()) {
                sysPage.setIsDefault(false);
            }
            if (null == sysPage.getIsMenu()) {
                sysPage.setIsMenu(false);
            }
            if (null == sysPage.getIsBlank()) {
                sysPage.setIsBlank(false);
            }
            this.pageService.updateById(sysPage);
        }
        return Result.ok();
    }

    @PostMapping("del")
    @ResponseBody
    public Result<?> del(@RequestParam(value = "ids") final String ids) throws Exception {
        final List<Long> idList = CommonTool.parseList(ids, ",", Long.class);
        this.pageService.removeByIds(new HashSet<>(idList));
        return Result.ok();
    }
}