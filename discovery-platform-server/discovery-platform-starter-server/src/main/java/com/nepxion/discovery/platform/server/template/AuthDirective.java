package com.nepxion.discovery.platform.server.template;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Ning Zhang
 * @version 1.0
 */

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.nepxion.discovery.platform.server.entity.vo.AdminVo;
import com.nepxion.discovery.platform.server.entity.vo.PageVo;

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
                return checkPermission(uri, PageVo::getCanInsert);
            case DELETE:
                return checkPermission(uri, PageVo::getCanDelete);
            case UPDATE:
                return checkPermission(uri, PageVo::getCanUpdate);
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
            uri = "";
        }
        PageVo pageVo = getByUri(adminVo.getPermissions(), uri);
        if (pageVo != null) {
            return handlePermission.check(pageVo);
        }
        return false;
    }

    private PageVo getByUri(List<PageVo> pageVoList,
                            String uri) {
        for (PageVo pageVo : pageVoList) {
            if (pageVo.getUrl().equals(uri)) {
                return pageVo;
            } else if (pageVo.getChildren() != null && !pageVo.getChildren().isEmpty()) {
                return getByUri(pageVo.getChildren(), uri);
            }
        }
        return null;
    }

    protected enum Operation {
        INSERT,
        DELETE,
        UPDATE,
        SELECT
    }

    private interface HandlePermission {
        boolean check(PageVo pageVo);
    }
}