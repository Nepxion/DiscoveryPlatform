package com.nepxion.discovery.platform.starter.server.template;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.platform.starter.server.entity.vo.Admin;
import com.nepxion.discovery.platform.starter.server.entity.vo.Page;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class AuthDirective {

    @Autowired
    protected HttpServletRequest request;

    protected boolean checkPermission(final Operation operation) {
        if (null == operation) {
            return false;
        }
        String uri = request.getRequestURI();
        switch (operation) {
            case INSERT:
                return checkPermission(uri, Page::getCanInsert);
            case DELETE:
                return checkPermission(uri, Page::getCanDelete);
            case UPDATE:
                return checkPermission(uri, Page::getCanUpdate);
            case SELECT:
                return checkPermission(uri, (permission) ->
                        permission.getCanDelete() ||
                                permission.getCanInsert() ||
                                permission.getCanUpdate() ||
                                permission.getCanSelect());
        }
        return false;
    }

    private boolean checkPermission(String uri,
                                    final HandlePermission handlePermission) {
        final Admin admin = (Admin) SecurityUtils.getSubject().getPrincipal();
        if (null == admin) {
            return false;
        } else if (admin.getSysRole().getSuperAdmin()) {
            return true;
        } else if (admin.getPermissions() == null || admin.getPermissions().size() < 1) {
            return false;
        }
        if (null == uri) {
            uri = "";
        }
        final Page page = getByUri(admin.getPermissions(), uri);
        if (null != page) {
            return handlePermission.check(page);
        }
        return false;
    }

    private Page getByUri(final List<Page> pageVoList,
                          final String uri) {
        for (final Page page : pageVoList) {
            if (page.getUrl().equals(uri)) {
                return page;
            } else if (null != page.getChildren() && !page.getChildren().isEmpty()) {
                return getByUri(page.getChildren(), uri);
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
        boolean check(final Page page);
    }
}