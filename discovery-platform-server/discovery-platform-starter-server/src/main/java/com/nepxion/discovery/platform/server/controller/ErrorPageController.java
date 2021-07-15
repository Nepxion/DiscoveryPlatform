package com.nepxion.discovery.platform.server.controller;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author rotten
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.tool.CommonTool;
import io.swagger.annotations.Api;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@Api("错误页相关接口")
@RestController
@RequestMapping(value = ErrorPageController.PREFIX)
public class ErrorPageController {
    public static final String PREFIX = "error";

    @GetMapping(value = "/404.do")
    public ModelAndView notFund(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("msg","功能模块正在开发中，敬请期待~");
        modelAndView.setViewName("/404");
        return modelAndView;
    }

}
