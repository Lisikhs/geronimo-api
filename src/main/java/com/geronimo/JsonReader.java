package com.geronimo;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonReader {
    private JsonNode jsonNode;

    public JsonReader(String data) throws IOException {
        jsonNode = new ObjectMapper().findAndRegisterModules().readTree(data);
    }

    public String readText(String property) {
        return jsonNode.get(property).asText();
    }

    public <T> T readObject(String property, Class<T> objClass) throws IOException {
        JsonNode value = jsonNode.findValue(property);
        return new ObjectMapper().findAndRegisterModules().treeToValue(value, objClass);
    }

    public <T> T readObject(Class<T> objClass) throws IOException {
        return new ObjectMapper().findAndRegisterModules().treeToValue(jsonNode, objClass);
    }
}
