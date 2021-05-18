package com.nepxion.discovery.platform.server.ldap.adapter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.adapter.LoginAdapter;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;

public class PlatformLdapLoginAdapter implements LoginAdapter {
    @Override
    public LoginMode getLoginMode() {
        return LoginMode.LDAP;
    }
}