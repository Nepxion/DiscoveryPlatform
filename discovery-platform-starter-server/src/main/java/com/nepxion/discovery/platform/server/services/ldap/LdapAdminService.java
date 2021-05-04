package com.nepxion.discovery.platform.server.services.ldap;

import com.nepxion.discovery.platform.server.entity.dto.SysAdmin;
import com.nepxion.discovery.platform.server.entity.vo.Admin;
import com.nepxion.discovery.platform.server.entity.vo.LdapUser;
import com.nepxion.discovery.platform.server.enums.Mode;
import com.nepxion.discovery.platform.server.ineterfaces.AdminService;
import com.nepxion.discovery.platform.server.services.LdapService;
import com.nepxion.discovery.platform.server.services.db.DbAdminService;
import com.nepxion.discovery.platform.tool.anno.TranSave;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

public class LdapAdminService extends DbAdminService {
    @Autowired
    private LdapService ldapService;
    @Autowired
    private AdminService adminService;

    @Override
    public boolean authenticate(String username, String password) throws Exception {
        return this.ldapService.authenticate(username, password);
    }

    @TranSave
    @Override
    public Admin getAdminByUserName(String username) throws Exception {
        final LdapUser ldapUser = this.ldapService.getByUserName(username);
        if (null == ldapUser) {
            return null;
        }
        SysAdmin sysAdmin = this.adminService.getByUserName(username);
        if (null == sysAdmin) {
            this.adminService.insert(Mode.LDAP, 2L, username, "", ldapUser.getName(), ldapUser.getPhoneNumber(), ldapUser.getEmail(), ldapUser.getRemark());
        } else {
            this.adminService.update(sysAdmin.getId(), sysAdmin.getSysRoleId(), sysAdmin.getUsername(), ldapUser.getName(), ldapUser.getPhoneNumber(), ldapUser.getEmail(), ldapUser.getRemark());
        }
        return this.adminService.getAdminByUserName(username);
    }

    @Override
    public List<Admin> search(final String keyword,
                              final int offset,
                              final int limit) throws Exception {
        final List<Admin> result = new ArrayList<>();
        final List<LdapUser> ldapUsersList = this.ldapService.search(keyword, offset, limit);

        for (final LdapUser ldapUser : ldapUsersList) {
            final Admin admin = new Admin();
            admin.setLoginMode(Mode.LDAP.getCode());
            admin.setUsername(ldapUser.getUsername());
            admin.setName(ldapUser.getName());
            admin.setPhoneNumber(ldapUser.getPhoneNumber());
            admin.setEmail(ldapUser.getEmail());
            admin.setRemark(ldapUser.getRemark());
            result.add(admin);
        }

        return result;
    }
}
