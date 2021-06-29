package com.nepxion.discovery.platform.server.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Objects;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

@ConfigurationProperties(prefix = "platform.server.auth")
public class PlatformAuthProperties {

    private TokenProperties token = new TokenProperties();

    public TokenProperties getToken() {
        return token;
    }

    public void setToken(TokenProperties token) {
        this.token = token;
    }

    public static class TokenProperties {

        private String secret;

        private Duration expireTime = Duration.ofMinutes(30L);

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
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TokenProperties that = (TokenProperties) o;
            return Objects.equals(getSecret(), that.getSecret()) && Objects.equals(getExpireTime(), that.getExpireTime()) && Objects.equals(getMaxLiveTime(), that.getMaxLiveTime()) && Objects.equals(getRenewThreshold(), that.getRenewThreshold());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getSecret(), getExpireTime(), getMaxLiveTime(), getRenewThreshold());
        }
    }

}
