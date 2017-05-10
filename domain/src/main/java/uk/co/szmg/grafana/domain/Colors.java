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

/**
 * Grafana color triplet used in singlestat.
 */
@JsonSerialize(using = Colors.ColorsSerialiser.class)
public class Colors {

    private String low;
    private String mid;
    private String high;

    /**
     * Default constructor with default colors: red, yellow, green.
     */
    public Colors() {
        this("rgba(245, 54, 54, 0.9)", "rgba(237, 129, 40, 0.89)", "rgba(50, 172, 45, 0.97)");
    }

    /**
     * Convenience constructor.
     * @param low color used when the value is low
     * @param mid color used when the value is medium
     * @param high color used when the value is high
     */
    public Colors(String low, String mid, String high) {
        this.low = low;
        this.mid = mid;
        this.high = high;
    }

    /**
     * Gets color used when the value is low.
     * @return color
     */
    public String getLow() {
        return low;
    }

    /**
     * Sets color used when the value is low.
     * @param low color that's meaningful in HTML
     */
    public void setLow(String low) {
        this.low = low;
    }

    /**
     * Gets color used when the value is medium.
     * @return color
     */
    public String getMid() {
        return mid;
    }

    /**
     * Sets color used when the value is medium.
     * @param mid color that's meaningful in HTML
     */
    public void setMid(String mid) {
        this.mid = mid;
    }

    /**
     * Gets color used when the value is high.
     * @return color
     */
    public String getHigh() {
        return high;
    }

    /**
     * Sets color used when the value is high.
     * @param high color that's meaningful in HTML
     */
    public void setHigh(String high) {
        this.high = high;
    }

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
