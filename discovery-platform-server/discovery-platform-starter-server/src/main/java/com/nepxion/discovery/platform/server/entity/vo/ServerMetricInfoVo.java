package com.nepxion.discovery.platform.server.entity.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @program: discovery-platform
 * @description:
 * @author: xiaolong
 * @create: 2021-07-17 10:07
 **/
public class ServerMetricInfoVo implements Serializable {

    private static final long serialVersionUID = 419448198396758148L;

    private String serviceType;
    private String serviceId;
    private Integer instanceNum;
    private List<InstanceInfo> instanceInfos;

    public static class InstanceInfo {
        private String serviceType;
        private String serviceId;
        private String version;
        private String region;
        private String environment;
        private String zone;
        private String host;
        private String port;
        private Map<String, String> metadata;

        public String getServiceType() {
            return serviceType;
        }

        public String getServiceId() {
            return serviceId;
        }

        public String getVersion() {
            return version;
        }

        public String getRegion() {
            return region;
        }

        public String getEnvironment() {
            return environment;
        }

        public String getZone() {
            return zone;
        }

        public String getHost() {
            return host;
        }

        public String getPort() {
            return port;
        }

        public Map<String, String> getMetadata() {
            return metadata;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public void setEnvironment(String environment) {
            this.environment = environment;
        }

        public void setZone(String zone) {
            this.zone = zone;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public void setMetadata(Map<String, String> metadata) {
            this.metadata = metadata;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }

        public void setServiceId(String serviceId) {
            this.serviceId = serviceId;
        }
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public Integer getInstanceNum() {
        return instanceNum;
    }

    public List<InstanceInfo> getInstanceInfos() {
        return instanceInfos;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setInstanceNum(Integer instanceNum) {
        this.instanceNum = instanceNum;
    }

    public void setInstanceInfos(List<InstanceInfo> instanceInfos) {
        this.instanceInfos = instanceInfos;
    }
}
