package com.nepxion.discovery.platform.server.state.handler;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Arrays;
import java.util.List;

import com.nepxion.discovery.platform.server.state.enums.Events;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateResolver {
    public static final List<Events> STATE_INITIAL_PUBLISHED_NEXT_EVENTS = Arrays.asList(Events.EVENT_DO_ADD, Events.EVENT_DO_MODIFY, Events.EVENT_DO_DELETE);
    public static final List<Events> STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_EVENTS = Arrays.asList(Events.EVENT_DO_ROLLBACK, Events.EVENT_DO_PUBLISH);
    public static final List<Events> EMPTY_NEXT_EVENTS = Arrays.asList();

    public static List<Events> getNextEvents(States state) {
        switch (state) {
            case STATE_INITIAL:
                return STATE_INITIAL_PUBLISHED_NEXT_EVENTS;
            case STATE_AWAIT_ADD:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_EVENTS;
            case STATE_AWAIT_MODIFY:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_EVENTS;
            case STATE_AWAIT_DELETE:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_EVENTS;
            case STATE_PUBLISHED:
                return STATE_INITIAL_PUBLISHED_NEXT_EVENTS;
        }

        return EMPTY_NEXT_EVENTS;
    }
}