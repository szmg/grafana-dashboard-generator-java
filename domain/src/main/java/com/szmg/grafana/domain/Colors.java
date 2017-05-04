package com.szmg.grafana.domain;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;

@JsonSerialize(using = Colors.ColorsSerialiser.class)
public class Colors {

    private String low = "rgba(245, 54, 54, 0.9)";
    private String mid = "rgba(237, 129, 40, 0.89)";
    private String high = "rgba(50, 172, 45, 0.97)";

    public static class ColorsSerialiser extends JsonSerializer<Colors> {

        @Override
        public void serialize(Colors colors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
            jsonGenerator.writeStartArray();
            jsonGenerator.writeString(colors.low);
            jsonGenerator.writeString(colors.mid);
            jsonGenerator.writeString(colors.high);
            jsonGenerator.writeEndArray();
        }
    }
}
