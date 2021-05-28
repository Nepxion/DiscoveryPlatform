package com.nepxion.discovery.platform.server.mysql.adapter;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.adapter.PlatformLoginAdapter;
import com.nepxion.discovery.platform.server.entity.enums.LoginMode;

public class PlatformMySqlLoginAdapter implements PlatformLoginAdapter {
    @Override
    public LoginMode getLoginMode() {
        return LoginMode.DATABASE;
    }
}