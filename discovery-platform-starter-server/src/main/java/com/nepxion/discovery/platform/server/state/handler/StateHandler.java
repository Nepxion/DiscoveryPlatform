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
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.LifecycleObjectSupport;

import com.nepxion.discovery.platform.server.state.context.StateContext;
import com.nepxion.discovery.platform.server.state.context.StateMachineContext;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateHandler extends LifecycleObjectSupport {
    private CompositeStateChangeListener compositeStateChangeListener;
    private StateChangeInterceptor stateChangeInterceptor;

    @Autowired
    private StateMachineFactory<States, Events> stateMachineFactory;

    @Autowired(required = false)
    private List<StateChangeListener> stateChangeListeners;

    public StateHandler() {
        compositeStateChangeListener = new CompositeStateChangeListener();
        stateChangeInterceptor = new StateChangeInterceptor(compositeStateChangeListener);
    }

    @Override
    protected void onInit() throws Exception {
        if (CollectionUtils.isNotEmpty(stateChangeListeners)) {
            for (StateChangeListener stateChangeListener : stateChangeListeners) {
                compositeStateChangeListener.register(stateChangeListener);
            }
        }
    }

    private StateMachine<States, Events> getStateMachine() {
        StateMachine<States, Events> stateMachine = StateMachineContext.getCurrentContext().getStateMachine();
        if (stateMachine == null) {
            stateMachine = stateMachineFactory.getStateMachine(UUID.randomUUID());

            stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.addStateMachineInterceptor(stateChangeInterceptor));

            StateMachineContext.getCurrentContext().setStateMachine(stateMachine);
        }

        return stateMachine;
    }

    public StateMessage<Events> sendSync(StateMessage<Events> requestMessage) {
        sendAsync(requestMessage);

        StateMessage<Events> responseMessage = StateContext.getCurrentContext().getMessage();

        StateContext.clearCurrentContext();

        return responseMessage;
    }

    public void sendAsync(StateMessage<Events> message) {
        if (message == null) {
            throw new IllegalArgumentException("Message is null");
        }

        StateMachine<States, Events> stateMachine = getStateMachine();

        boolean result = stateMachine.sendEvent(message);
        if (!result) {
            throw new IllegalArgumentException("Message is rejected with illegal state=" + message.getSourceState() + ", event=" + message.getPayload());
        }
    }
}