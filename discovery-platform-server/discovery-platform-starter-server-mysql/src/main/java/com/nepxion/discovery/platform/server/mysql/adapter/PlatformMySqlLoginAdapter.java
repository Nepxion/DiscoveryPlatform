package com.nepxion.discovery.platform.server.mysql.adapter;

import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;

public class PlatformMySqlLoginAdapter implements PlatformLoginAdapter {
    @Override
    public LoginMode getLoginMode() {
        return LoginMode.DATABASE;
    }
}