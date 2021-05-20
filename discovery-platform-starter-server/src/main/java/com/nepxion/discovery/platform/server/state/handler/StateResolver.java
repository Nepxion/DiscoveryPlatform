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

import com.nepxion.discovery.platform.server.state.enums.Actions;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateResolver {
    public static final List<Actions> STATE_INITIAL_PUBLISHED_NEXT_ACTIONS = Arrays.asList(Actions.ACTION_TO_ADD, Actions.ACTION_TO_MODIFY, Actions.ACTION_TO_DELETE);
    public static final List<Actions> STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_ACTIONS = Arrays.asList(Actions.ACTION_TO_ROLLBACK, Actions.ACTION_TO_PUBLISH);
    public static final List<Actions> EMPTY_NEXT_ACTIONS = Arrays.asList();

    public static List<Actions> getNextActions(States state) {
        switch (state) {
            case STATE_INITIAL:
                return STATE_INITIAL_PUBLISHED_NEXT_ACTIONS;
            case STATE_TO_ADD:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_ACTIONS;
            case STATE_TO_MODIFY:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_ACTIONS;
            case STATE_TO_DELETE:
                return STATE_STATE_TO_ADD_MODIFY_DELETE_NEXT_ACTIONS;
            case STATE_PUBLISHED:
                return STATE_INITIAL_PUBLISHED_NEXT_ACTIONS;
        }

        return EMPTY_NEXT_ACTIONS;
    }
}