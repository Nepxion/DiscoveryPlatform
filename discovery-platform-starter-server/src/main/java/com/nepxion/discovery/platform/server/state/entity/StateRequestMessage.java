package com.nepxion.discovery.platform.server.state.entity;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 * @author Haojun Ren
 * @version 1.0
 */

import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;

import com.nepxion.discovery.platform.server.state.enums.States;

public class StateRequestMessage<T> extends GenericMessage<T> {
    private static final long serialVersionUID = -480682171835714441L;

    // 当前状态
    private States state;

    public StateRequestMessage(States state, T payload) {
        this(payload);

        this.state = state;
    }

    public StateRequestMessage(States state, T payload, Map<String, Object> headers) {
        this(payload, headers);

        this.state = state;
    }

    public StateRequestMessage(States state, T payload, MessageHeaders headers) {
        this(payload, headers);

        this.state = state;
    }

    public StateRequestMessage(T payload) {
        super(payload);
    }

    public StateRequestMessage(T payload, Map<String, Object> headers) {
        super(payload, headers);
    }

    public StateRequestMessage(T payload, MessageHeaders headers) {
        super(payload, headers);
    }

    public States getState() {
        return state;
    }

    public void setState(States state) {
        this.state = state;
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