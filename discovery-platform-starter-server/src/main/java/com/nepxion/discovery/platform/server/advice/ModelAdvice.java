package com.nepxion.discovery.platform.server.advice;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.configuration.properties.PlatformProperties;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ModelAdvice {
    private final PlatformProperties platformProperties;

    public ModelAdvice(final PlatformProperties platformProperties) {
        this.platformProperties = platformProperties;
    }

    @ModelAttribute
    public void addAttributes(final Model model) {
        model.addAttribute("title", this.platformProperties.getTitle());
        model.addAttribute("fullName", this.platformProperties.getFullName());
        model.addAttribute("shortName", this.platformProperties.getShortName());
    }
}