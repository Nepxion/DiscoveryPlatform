package com.nepxion.discovery.platform.server.entity.po;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.nepxion.discovery.common.util.JsonUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.*;

public class RouteGatewayPo implements Serializable {
    private static final long serialVersionUID = 8552414941889295451L;

    private String id;
    private String uri;
    private List<String> predicates = new ArrayList<String>();
    private List<Predicate> userPredicates = new ArrayList<Predicate>();
    private List<String> filters = new ArrayList<String>();
    private List<Filter> userFilters = new ArrayList<Filter>();
    private int order = 0;
    private Map<String, Object> metadata = new HashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<String> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<String> predicates) {
        this.predicates = predicates;
    }

    public List<Predicate> getUserPredicates() {
        return userPredicates;
    }

    public void setUserPredicates(List<Predicate> userPredicates) {
        this.userPredicates = userPredicates;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public List<Filter> getUserFilters() {
        return userFilters;
    }

    public void setUserFilters(List<Filter> userFilters) {
        this.userFilters = userFilters;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        if (metadata != null) {
            this.metadata = metadata;
        }
    }

    public String getUserPredicatesStr() {
        StringBuilder userPredicateStringBuilder = new StringBuilder();
        for (Predicate predicate : this.getUserPredicates()) {
            userPredicateStringBuilder.append(String.format("%s=%s, ", predicate.getName(), JsonUtil.toJson(predicate.getArgs())));
        }
        if (userPredicateStringBuilder.length() > 0) {
            userPredicateStringBuilder.delete(userPredicateStringBuilder.length() - 2, userPredicateStringBuilder.length());
        }
        return userPredicateStringBuilder.toString();
    }

    public String getUserFiltersStr() {
        StringBuilder userFilterStringBuilder = new StringBuilder();
        for (Filter filter : this.getUserFilters()) {
            userFilterStringBuilder.append(String.format("%s=%s, ", filter.getName(), JsonUtil.toJson(filter.getArgs())));
        }
        if (userFilterStringBuilder.length() > 0) {
            userFilterStringBuilder.delete(userFilterStringBuilder.length() - 2, userFilterStringBuilder.length());
        }
        return userFilterStringBuilder.toString();
    }

    public static class BaseClauseEntity {
        private String name;
        private Map<String, String> args = new LinkedHashMap<>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getArgs() {
            return args;
        }

        public void setArgs(Map<String, String> args) {
            this.args = args;
        }

        public void addArg(String key, String value) {
            this.args.put(key, value);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object object) {
            return EqualsBuilder.reflectionEquals(this, object);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    public static class Predicate extends BaseClauseEntity {
        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object object) {
            return EqualsBuilder.reflectionEquals(this, object);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    public static class Filter extends BaseClauseEntity {
        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object object) {
            return EqualsBuilder.reflectionEquals(this, object);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
        }
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}