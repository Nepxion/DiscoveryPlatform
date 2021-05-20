package com.nepxion.discovery.platform.server.state.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import com.nepxion.discovery.platform.server.state.enums.Actions;
import com.nepxion.discovery.platform.server.state.enums.States;

public class StateMessage<T> extends GenericMessage<T> {
    private static final long serialVersionUID = 3822349578970192942L;

    // 上一个状态
    private States sourceState;

    // 下一个状态
    private States targetState;

    // 下一个状态下，可选择的操作列表
    private List<Actions> nextActions;

    public StateMessage(States sourceState, T payload) {
        this(payload);

        this.sourceState = sourceState;
    }

    public StateMessage(States sourceState, T payload, Map<String, Object> headers) {
        this(payload, headers);

        this.sourceState = sourceState;
    }

    public StateMessage(States sourceState, T payload, MessageHeaders headers) {
        this(payload, headers);

        this.sourceState = sourceState;
    }

    public StateMessage(T payload) {
        super(payload);
    }

    public StateMessage(T payload, Map<String, Object> headers) {
        super(payload, headers);
    }

    public StateMessage(T payload, MessageHeaders headers) {
        super(payload, headers);
    }

    public States getSourceState() {
        return sourceState;
    }

    public void setSourceState(States sourceState) {
        this.sourceState = sourceState;
    }

    public States getTargetState() {
        return targetState;
    }

    public void setTargetState(States targetState) {
        this.targetState = targetState;
    }

    public List<Actions> getNextActions() {
        return nextActions;
    }

    public void setNextActions(List<Actions> nextActions) {
        this.nextActions = nextActions;
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}