package com.nepxion.discovery.platform.server.entity.po;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Hui Liu
 * @version 1.0
 */

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.nepxion.discovery.common.entity.PortalType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel("链路侦测模拟参数对象")
public class RequestSimulationPo implements Serializable {
    private static final long serialVersionUID = -1L;

    @ApiModelProperty("侦测入口类型")
    private PortalType portalType;

    @ApiModelProperty("侦测入口协议")
    private String portalProtocol;

    @ApiModelProperty("侦测入口服务")
    private String portalService;

    @ApiModelProperty("侦测入口实例")
    private String portalInstance;


    @ApiModelProperty("侦测参数-header")
    private Map<String, String> headers;

    @ApiModelProperty("侦测参数-parameter")
    private Map<String, String> parameters;

    @ApiModelProperty("侦测参数-cookie")
    private Map<String, String> cookies;


    @ApiModelProperty("侦测链路")
    private List<String> serviceChain;

    @ApiModelProperty("侦测类型：版本、区域、分组、环境、可用区、地址等")
    private String dimension;

    @ApiModelProperty("请求次数")
    private Integer requestTimes;

    public PortalType getPortalType() {
        return portalType;
    }

    public void setPortalType(PortalType portalType) {
        this.portalType = portalType;
    }

    public String getPortalProtocol() {
        return portalProtocol;
    }

    public void setPortalProtocol(String portalProtocol) {
        this.portalProtocol = portalProtocol;
    }

    public String getPortalService() {
        return portalService;
    }

    public void setPortalService(String portalService) {
        this.portalService = portalService;
    }

    public String getPortalInstance() {
        return portalInstance;
    }

    public void setPortalInstance(String portalInstance) {
        this.portalInstance = portalInstance;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

    public List<String> getServiceChain() {
        return serviceChain;
    }

    public void setServiceChain(List<String> serviceChain) {
        this.serviceChain = serviceChain;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Integer getRequestTimes() {
        return requestTimes;
    }

    public void setRequestTimes(Integer requestTimes) {
        this.requestTimes = requestTimes;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
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
