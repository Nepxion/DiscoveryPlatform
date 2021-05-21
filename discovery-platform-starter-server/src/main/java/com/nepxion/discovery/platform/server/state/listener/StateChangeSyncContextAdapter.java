package com.nepxion.discovery.platform.server.state.listener;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import com.nepxion.discovery.platform.server.state.context.StateContext;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.handler.AbstractStateChangeAdapter;

public class StateChangeSyncContextAdapter extends AbstractStateChangeAdapter {
    @Override
    public void onChanged(StateMessage<Events> message) {
        StateContext.getCurrentContext().setMessage(message);
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
    }
}