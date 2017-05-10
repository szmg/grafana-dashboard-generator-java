package uk.co.szmg.grafana.domain;

/*-
 * #%L
 * domain
 * %%
 * Copyright (C) 2017 Mate Gabor Szvoboda
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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
 * custom values using {@link #setValue(String, Object)} or {@link #withValue(String, Object)}.
 * (Or "change" the type of a known field.
 *
 * Note that this class is mutable.
 */
@JsonSerialize(using = BaseJsonObject.BaseJsonObjectSerializer.class)
public abstract class BaseJsonObject<C extends BaseJsonObject<C>> {

    private Map<String, Object> fields = new HashMap<>();

    /**
     * Sets a given field.
     * @param fieldName field name, i.e., JSON property name when serialised
     * @param value value, which should be serializable by Jackson; {@code null} values will not be included
     */
    public void setValue(String fieldName, Object value) {
        fields.put(fieldName, value);
    }

    /**
     * Fluent setter of a given field.
     * @param fieldName field name, i.e., JSON property name when serialised
     * @param value value, which should be serializable by Jackson; {@code null} values will not be included
     * @return itself, so it is chainable
     */
    public C withValue(String fieldName, Object value) {
        fields.put(fieldName, value);
        return (C) this;
    }

    /**
     * Gets value of the given field, or {@code null} if not set.
     * @param fieldName field name, i.e., JSON property name when serialised
     * @param <T> expected type of the value
     * @return the value if it has been set, {@code null} otherwise
     */
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
