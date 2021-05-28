package com.nepxion.discovery.platform.server.advice;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.nepxion.discovery.platform.server.constant.PlatformConstant;
import com.nepxion.discovery.platform.server.properties.PlatformServerProperties;

@ControllerAdvice
public class ModelAdvice {
    @Autowired
    private PlatformServerProperties platformProperties;

    @ModelAttribute
    public void addAttributes(Model model) {
        model.addAttribute(PlatformConstant.TITLE, this.platformProperties.getTitle());
        model.addAttribute(PlatformConstant.FULL_NAME, this.platformProperties.getFullName());
        model.addAttribute(PlatformConstant.SHORT_NAME, this.platformProperties.getShortName());
    }
}