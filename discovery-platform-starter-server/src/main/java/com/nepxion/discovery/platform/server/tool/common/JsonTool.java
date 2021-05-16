package com.nepxion.discovery.platform.server.tool.common;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.util.List;

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
