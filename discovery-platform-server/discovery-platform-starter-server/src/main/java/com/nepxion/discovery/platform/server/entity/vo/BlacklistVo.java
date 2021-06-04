package com.nepxion.discovery.platform.server.entity.vo;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nepxion.discovery.common.util.StringUtil;
import com.nepxion.discovery.platform.server.entity.po.BlacklistPo;

public class BlacklistVo implements Serializable {
    private static final long serialVersionUID = 2983232551489718047L;

    private String host;
    private String port;
    private BlacklistPoVo blacklist;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public BlacklistPoVo getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(BlacklistPoVo blacklist) {
        this.blacklist = blacklist;
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

    public static class BlacklistPoVo extends BlacklistPo {
        private static final long serialVersionUID = -2816083419230413910L;

        public Map<String, List<String>> getIdMap() {
            return StringUtil.splitToComplexMap(getIdValue());
        }

        public Map<String, List<String>> getAddressMap() {
            return StringUtil.splitToComplexMap(getAddressValue());
        }
    }
}