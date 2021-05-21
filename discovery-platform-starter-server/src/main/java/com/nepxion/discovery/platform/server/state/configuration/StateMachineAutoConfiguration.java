package com.nepxion.discovery.platform.server.state.configuration;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.EnumSet;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;
import com.nepxion.discovery.platform.server.state.handler.StateChangeAdapter;
import com.nepxion.discovery.platform.server.state.handler.StateChangeListener;
import com.nepxion.discovery.platform.server.state.handler.StateChangeListenerImpl;
import com.nepxion.discovery.platform.server.state.handler.StateHandler;
import com.nepxion.discovery.platform.server.state.listener.StateChangePublisherAdapter;
import com.nepxion.discovery.platform.server.state.listener.StateChangeSyncContextAdapter;

@Configuration
@EnableStateMachineFactory
public class StateMachineAutoConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {
    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.STATE_INITIAL)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.STATE_INITIAL).target(States.STATE_TO_ADD)
                .event(Events.EVENT_DO_ADD)
                .and()
                .withExternal()
                .source(States.STATE_INITIAL).target(States.STATE_TO_MODIFY)
                .event(Events.EVENT_DO_MODIFY)
                .and()
                .withExternal()
                .source(States.STATE_INITIAL).target(States.STATE_TO_DELETE)
                .event(Events.EVENT_DO_DELETE)
                .and()
                .withExternal()
                .source(States.STATE_PUBLISHED).target(States.STATE_TO_ADD)
                .event(Events.EVENT_DO_ADD)
                .and()
                .withExternal()
                .source(States.STATE_PUBLISHED).target(States.STATE_TO_MODIFY)
                .event(Events.EVENT_DO_MODIFY)
                .and()
                .withExternal()
                .source(States.STATE_PUBLISHED).target(States.STATE_TO_DELETE)
                .event(Events.EVENT_DO_DELETE)
                .and()
                .withExternal()
                .source(States.STATE_TO_ADD).target(States.STATE_PUBLISHED)
                .event(Events.EVENT_DO_PUBLISH)
                .and()
                .withExternal()
                .source(States.STATE_TO_MODIFY).target(States.STATE_PUBLISHED)
                .event(Events.EVENT_DO_PUBLISH)
                .and()
                .withExternal()
                .source(States.STATE_TO_DELETE).target(States.STATE_PUBLISHED)
                .event(Events.EVENT_DO_PUBLISH)
                .and()
                .withExternal()
                .source(States.STATE_TO_ADD).target(States.STATE_INITIAL)
                .event(Events.EVENT_DO_ROLLBACK)
                .and()
                .withExternal()
                .source(States.STATE_TO_MODIFY).target(States.STATE_INITIAL)
                .event(Events.EVENT_DO_ROLLBACK)
                .and()
                .withExternal()
                .source(States.STATE_TO_DELETE).target(States.STATE_INITIAL)
                .event(Events.EVENT_DO_ROLLBACK)
                .and()
                .withExternal()
                .source(States.STATE_TO_ADD).target(States.STATE_TO_MODIFY)
                .event(Events.EVENT_DO_MODIFY)
                .and()
                .withExternal()
                .source(States.STATE_TO_ADD).target(States.STATE_TO_DELETE)
                .event(Events.EVENT_DO_DELETE)
                .and()
                .withExternal()
                .source(States.STATE_TO_MODIFY).target(States.STATE_TO_DELETE)
                .event(Events.EVENT_DO_DELETE)
                .and()
                .withExternal()
                .source(States.STATE_TO_DELETE).target(States.STATE_TO_MODIFY)
                .event(Events.EVENT_DO_MODIFY);
    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config)
            throws Exception {
        config
                .withConfiguration()
                .autoStartup(true);
    }

    @Bean
    public StateHandler stateHandler() {
        return new StateHandler();
    }

    @Bean
    public StateChangeListener stateChangeListener() {
        return new StateChangeListenerImpl();
    }

    @Bean
    public StateChangeAdapter stateChangeSyncContextAdapter() {
        return new StateChangeSyncContextAdapter();
    }

    @Bean
    public StateChangeAdapter stateChangePublisherAdapter() {
        return new StateChangePublisherAdapter();
    }
}