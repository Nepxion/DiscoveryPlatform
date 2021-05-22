package com.nepxion.discovery.platform.server.advice;

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
    public void addAttributes(final Model model) {
        model.addAttribute(PlatformConstant.TITLE, this.platformProperties.getTitle());
        model.addAttribute(PlatformConstant.FULL_NAME, this.platformProperties.getFullName());
        model.addAttribute(PlatformConstant.SHORT_NAME, this.platformProperties.getShortName());
    }
}