package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.statemachine.transition.Transition;

import com.nepxion.discovery.platform.server.state.entity.StateMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateChangeInterceptor extends StateMachineInterceptorAdapter<States, Events> {
    private CompositeStateChangeListener compositeStateChangeListener;

    public StateChangeInterceptor(CompositeStateChangeListener compositeStateChangeListener) {
        this.compositeStateChangeListener = compositeStateChangeListener;
    }

    @Override
    public void preStateChange(State<States, Events> state, Message<Events> message, Transition<States, Events> transition, StateMachine<States, Events> stateMachine, StateMachine<States, Events> rootStateMachine) {
        States sourceState = transition.getSource().getId();
        States targetState = transition.getTarget().getId();

        StateMessage<Events> responseMessage = new StateMessage<Events>(message.getPayload(), message.getHeaders());

        responseMessage.setSourceState(sourceState);
        responseMessage.setTargetState(targetState);
        responseMessage.setNextEvents(StateResolver.getNextEvents(targetState));

        compositeStateChangeListener.onChanged(responseMessage, state, transition, stateMachine, rootStateMachine);
    }
}