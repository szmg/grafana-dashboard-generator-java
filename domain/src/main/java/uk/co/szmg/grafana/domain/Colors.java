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
