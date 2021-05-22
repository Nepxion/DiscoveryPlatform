package com.nepxion.discovery.platform.server.ldap.adapter;

import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;

public class PlatformLdapLoginAdapter implements PlatformLoginAdapter {
    @Override
    public LoginMode getLoginMode() {
        return LoginMode.LDAP;
    }
}