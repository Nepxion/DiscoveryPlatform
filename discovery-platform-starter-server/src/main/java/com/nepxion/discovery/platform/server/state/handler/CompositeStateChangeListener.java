package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Iterator;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.AbstractCompositeListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import com.nepxion.discovery.platform.server.state.entity.StateResponseMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class CompositeStateChangeListener extends AbstractCompositeListener<StateChangeListener> implements StateChangeListener {
    @Override
    public void onChanged(State<States, Events> state, StateResponseMessage<Events> message, Transition<States, Events> transition, StateMachine<States, Events> stateMachine, StateMachine<States, Events> rootStateMachine) {
        for (Iterator<StateChangeListener> iterator = getListeners().reverse(); iterator.hasNext();) {
            StateChangeListener stateChangeListener = iterator.next();

            stateChangeListener.onChanged(state, message, transition, stateMachine, rootStateMachine);
        }
    }
}