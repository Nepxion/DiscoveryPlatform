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
import com.nepxion.discovery.platform.server.state.entity.StateRequestMessage;
import com.nepxion.discovery.platform.server.state.entity.StateResponseMessage;
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

    public StateResponseMessage<Events> send(StateRequestMessage<Events> requestMessage) {
        sendEvent(requestMessage);

        StateResponseMessage<Events> responseMessage = StateMachineContext.getCurrentContext().getMessage();

        StateMachineContext.clearCurrentContext();

        return responseMessage;
    }

    public boolean sendEvent(StateRequestMessage<Events> requestMessage) {
        /*stateMachine.stop();
        List<StateMachineAccess<States, Events>> accesses = stateMachine.getStateMachineAccessor().withAllRegions();
        for (StateMachineAccess<States, Events> access : accesses) {
            access.resetStateMachine(new DefaultStateMachineContext<>(requestMessage.getTargetState(), null, null, null));
        }
        stateMachine.start();*/

        return stateMachine.sendEvent(requestMessage);
    }
}