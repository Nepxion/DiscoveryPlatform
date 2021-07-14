package com.nepxion.discovery.platform.server.service;

import com.nepxion.discovery.platform.server.entity.dto.BlueGreenDto;
import java.io.Serializable;
import org.springframework.stereotype.Service;

public class GrayServiceImpl implements GrayService {
	private String xmlConfig = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
		+ "<rule>\n"
		+ "    <discovery>\n"
		+ "        <version>\n"
		+ "            <!-- 表示网关g的1.0，允许访问提供端服务a的1.0版本 -->\n"
		+ "            <service consumer-service-name=\"discovery-springcloud-example-gateway\" provider-service-name=\"discovery-springcloud-example-a\" consumer-version-value=\"1.0\" provider-version-value=\"1.0\"/>\n"
		+ "            <!-- 表示网关g的1.1，允许访问提供端服务a的1.1版本 -->\n"
		+ "            <service consumer-service-name=\"discovery-springcloud-example-gateway\" provider-service-name=\"discovery-springcloud-example-a\" consumer-version-value=\"1.1\" provider-version-value=\"1.1\"/>\n"
		+ "        </version>\n"
		+ "    </discovery>\n"
		+ "</rule>";

	private String jsonConfig = "{\n"
		+ "    \"discoveryEntity\": {\n"
		+ "        \"versionFilterEntity\": {\n"
		+ "            \"versionEntityMap\": {\n"
		+ "                \"discovery-springcloud-example-gateway\": [\n"
		+ "                    {\n"
		+ "                        \"consumerServiceName\": \"discovery-springcloud-example-gateway\", \n"
		+ "                        \"providerServiceName\": \"discovery-springcloud-example-a\", \n"
		+ "                        \"consumerVersionValueList\": [\n"
		+ "                            \"1.0\"\n"
		+ "                        ], \n"
		+ "                        \"providerVersionValueList\": [\n"
		+ "                            \"1.0\"\n"
		+ "                        ]\n"
		+ "                    }, \n"
		+ "                    {\n"
		+ "                        \"consumerServiceName\": \"discovery-springcloud-example-gateway\", \n"
		+ "                        \"providerServiceName\": \"discovery-springcloud-example-a\", \n"
		+ "                        \"consumerVersionValueList\": [\n"
		+ "                            \"1.1\"\n"
		+ "                        ], \n"
		+ "                        \"providerVersionValueList\": [\n"
		+ "                            \"1.1\"\n"
		+ "                        ]\n"
		+ "                    }\n"
		+ "                ]\n"
		+ "            }\n"
		+ "        }\n"
		+ "    }\n"
		+ "}";

	@Override
	public String getGraySetting(Long id) {
		String xml = "";
		return jsonConfig;
	}

	@Override
	public BlueGreenDto getById(Serializable id) {
		return null;
	}

	@Override
	public void update(BlueGreenDto blueGreenDto) {

	}

	@Override
	public void enable(Serializable id, boolean enableFlag) {

	}
}
