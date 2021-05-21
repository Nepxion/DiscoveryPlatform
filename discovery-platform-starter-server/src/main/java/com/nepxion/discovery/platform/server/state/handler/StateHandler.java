package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.LifecycleObjectSupport;

import com.nepxion.discovery.platform.server.state.context.StateMachineContext;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateHandler extends LifecycleObjectSupport {
    private CompositeStateChangeListener compositeStateChangeListener;
    private StateChangeInterceptor stateChangeInterceptor;

    @Autowired
    private StateMachine<States, Events> stateMachine;

    @Autowired(required = false)
    private List<StateChangeListener> stateChangeListeners;

    public StateHandler() {
        compositeStateChangeListener = new CompositeStateChangeListener();
        stateChangeInterceptor = new StateChangeInterceptor(compositeStateChangeListener);
    }

    @Override
    protected void onInit() throws Exception {
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.addStateMachineInterceptor(stateChangeInterceptor));

        if (CollectionUtils.isNotEmpty(stateChangeListeners)) {
            for (StateChangeListener stateChangeListener : stateChangeListeners) {
                compositeStateChangeListener.register(stateChangeListener);
            }
        }
    }

    public StateMessage<Events> nextSync(StateMessage<Events> requestMessage) {
        nextAsync(requestMessage);

        StateMessage<Events> responseMessage = StateMachineContext.getCurrentContext().getMessage();

        StateMachineContext.clearCurrentContext();

        return responseMessage;
    }

    public void nextAsync(StateMessage<Events> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }

        boolean result = stateMachine.sendEvent(message);
        if (!result) {
            throw new IllegalArgumentException("Message is rejected with illegal state=" + message.getSourceState() + ", event=" + message.getPayload());
        }
    }
}