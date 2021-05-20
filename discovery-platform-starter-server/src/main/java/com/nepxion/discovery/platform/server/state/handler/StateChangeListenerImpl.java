package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import com.nepxion.discovery.platform.server.state.context.StateMachineContext;
import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateChangeListenerImpl implements StateChangeListener {
    private static final Logger LOG = LoggerFactory.getLogger(StateChangeListenerImpl.class);

    @Autowired(required = false)
    private StateChangeAdapter stateChangeAdapter;

    @Override
    public void onChanged(StateMessage<Events> message, State<States, Events> state, Transition<States, Events> transition, StateMachine<States, Events> stateMachine, StateMachine<States, Events> rootStateMachine) {
        LOG.info("State changed to publish message={}", message);

        StateMachineContext.getCurrentContext().setMessage(message);

        if (stateChangeAdapter != null) {
            stateChangeAdapter.onChanged(message);
        }
    }
}