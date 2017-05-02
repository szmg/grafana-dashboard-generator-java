package com.szmg.grafana.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@JsonSerialize(using = BaseJsonObject.BaseJsonObjectSerializer.class)
public abstract class BaseJsonObject {

    private Map<String, Object> fields = new HashMap<>();

    public void addValue(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public <T> T getValue(String fieldName) {
        return (T) fields.get(fieldName);
    }

    public static class BaseJsonObjectSerializer extends JsonSerializer<BaseJsonObject> {

        @Override
        public void serialize(BaseJsonObject baseJsonObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, Object> entry : baseJsonObject.fields.entrySet()) {
                jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
            }
            jsonGenerator.writeEndObject();
        }
    }

}
