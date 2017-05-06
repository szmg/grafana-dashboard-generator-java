package com.szmg.grafana.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * A naively simple Map wrapper. The goal was to be able to model JSON
 * objects that has known and unknown properties as well. This class
 * should be extended with the known fields, while one can always add
 * custom values using #addValue. (Or "change" the type of a known field.
 *
 * Note that this class is mutable.
 */
@JsonSerialize(using = BaseJsonObject.BaseJsonObjectSerializer.class)
public abstract class BaseJsonObject<C extends BaseJsonObject<C>> {

    private Map<String, Object> fields = new HashMap<>();

    public void setValue(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    public C withValue(String fieldName, Object value) {
        fields.put(fieldName, value);
        return (C) this;
    }

    public <T> T getValue(String fieldName) {
        return (T) fields.get(fieldName);
    }

    public static class BaseJsonObjectSerializer extends JsonSerializer<BaseJsonObject<?>> {

        @Override
        public void serialize(BaseJsonObject<?> baseJsonObject, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeStartObject();
            for (Map.Entry<String, Object> entry : baseJsonObject.fields.entrySet()) {
                jsonGenerator.writeObjectField(entry.getKey(), entry.getValue());
            }
            jsonGenerator.writeEndObject();
        }
    }

}
