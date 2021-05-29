package com.nepxion.discovery.platform.server.template;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.entity.enums.Operation;
import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.MenuVo;

public abstract class AuthDirective {
    @Autowired
    protected HttpServletRequest request;

    protected boolean checkPermission(Operation operation) {
        if (operation == null) {
            return false;
        }
        String uri = request.getRequestURI();
        switch (operation) {
            case INSERT:
                return checkPermission(uri, MenuVo::getCanInsert);
            case DELETE:
                return checkPermission(uri, MenuVo::getCanDelete);
            case UPDATE:
                return checkPermission(uri, MenuVo::getCanUpdate);
            case SELECT:
                return checkPermission(uri, (permission) ->
                        permission.getCanDelete() ||
                                permission.getCanInsert() ||
                                permission.getCanUpdate() ||
                                permission.getCanSelect());
        }
        return false;
    }

    private boolean checkPermission(String uri, HandlePermission handlePermission) {
        AdminVo adminVo = (AdminVo) SecurityUtils.getSubject().getPrincipal();
        if (adminVo == null) {
            return false;
        } else if (adminVo.getSysRole().getSuperAdmin()) {
            return true;
        } else if (adminVo.getPermissions() == null || adminVo.getPermissions().size() < 1) {
            return false;
        }
        if (uri == null) {
            uri = StringUtils.EMPTY;
        }
        MenuVo menuVo = getByUri(adminVo.getPermissions(), uri);
        if (menuVo != null) {
            return handlePermission.check(menuVo);
        }
        return false;
    }

    private MenuVo getByUri(List<MenuVo> menuVoList, String uri) {
        for (MenuVo menuVo : menuVoList) {
            if (menuVo.getUrl().equals(uri)) {
                return menuVo;
            } else if (menuVo.getChildren() != null && !menuVo.getChildren().isEmpty()) {
                return getByUri(menuVo.getChildren(), uri);
            }
        }
        return null;
    }

    private interface HandlePermission {
        boolean check(MenuVo menuVo);
    }
}