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

import com.nepxion.discovery.platform.server.state.entity.StateRequestMessage;
import com.nepxion.discovery.platform.server.state.entity.StateResponseMessage;
import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateChangeInterceptor extends StateMachineInterceptorAdapter<States, Events> {
    private CompositeStateChangeListener compositeStateChangeListener;

    public StateChangeInterceptor(CompositeStateChangeListener compositeStateChangeListener) {
        this.compositeStateChangeListener = compositeStateChangeListener;
    }

    @Override
    public void preStateChange(State<States, Events> state, Message<Events> message, Transition<States, Events> transition, StateMachine<States, Events> stateMachine, StateMachine<States, Events> rootStateMachine) {
        StateRequestMessage<Events> requestMessage = (StateRequestMessage<Events>) message;

        States sourceState = transition.getSource().getId();
        States targetState = transition.getTarget().getId();

        StateResponseMessage<Events> responseMessage = new StateResponseMessage<Events>(requestMessage.getPayload(), requestMessage.getHeaders());

        responseMessage.setFromState(sourceState);
        responseMessage.setToState(targetState);
        responseMessage.setNextActions(StateResolver.getNextActions(targetState));

        compositeStateChangeListener.onChanged(state, responseMessage, transition, stateMachine, rootStateMachine);
    }
}