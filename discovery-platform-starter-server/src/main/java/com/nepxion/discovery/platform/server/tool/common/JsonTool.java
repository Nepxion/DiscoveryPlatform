package com.nepxion.discovery.platform.server.tool.common;

/**
 * <p>Title: Nepxion Discovery</p>
 * <p>Description: Nepxion Discovery</p>
 * <p>Copyright: Copyright (c) 2017-2050</p>
 * <p>Company: Nepxion</p>
 *
 * @author Ning Zhang
 * @version 1.0
 */

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class JsonTool {
    private final static Gson GSON = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

    public static String prettyFormat(String json) {
        return GSON.toJson(JsonParser.parseString(json));
    }

    public static <T> T toEntity(String json,
                                       TypeToken<T> typeToken) {
        return GSON.fromJson(json, typeToken.getType());
    }
}