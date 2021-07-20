package com.nepxion.discovery.platform.server.properties;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

import java.time.Duration;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "platform.server.auth")
public class PlatformAuthProperties {

    private TokenProperties token = new TokenProperties();

    public TokenProperties getToken() {
        return token;
    }

    public void setToken(TokenProperties token) {
        this.token = token;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class TokenProperties {

        private String secret;

        private Duration expireTime = Duration.ofDays(1);

        private Duration maxLiveTime = Duration.ofDays(1);

        private Integer renewThreshold = 1;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public Duration getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(Duration expireTime) {
            this.expireTime = expireTime;
        }

        public Duration getMaxLiveTime() {
            return maxLiveTime;
        }

        public void setMaxLiveTime(Duration maxLiveTime) {
            this.maxLiveTime = maxLiveTime;
        }

        public Integer getRenewThreshold() {
            return renewThreshold;
        }

        public void setRenewThreshold(Integer renewThreshold) {
            this.renewThreshold = renewThreshold;
        }

        @Override
        public boolean equals(Object o) {
            return EqualsBuilder.reflectionEquals(this, o);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }

}
