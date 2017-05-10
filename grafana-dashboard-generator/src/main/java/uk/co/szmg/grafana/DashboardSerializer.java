package uk.co.szmg.grafana;

/*-
 * #%L
 * dashboard-builder-main
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Turns {@link Dashboard}s into their JSON representation.
 *
 * It does no validation.
 */
public class DashboardSerializer {

    private final ObjectMapper mapper;

    /**
     * Default constructor, which initialises Jackson ObjectMapper with pretty print option.
     */
    public DashboardSerializer() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Gets the String representation of the given dashboard.
     * The output is pretty printed.
     * @param dashboard dashboard to turn into String
     * @return String (JSON) representation of dashboard
     * @throws IllegalArgumentException when the dashboard cannot be serialised
     */
    public String toString(Dashboard dashboard) {
        try {
            return mapper.writeValueAsString(dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    /**
     * Writes the pretty printed JSON representation of a dashboard with the given {@link Writer}.
     * @param dashboard dashboard to be serialised
     * @param writer writer to be written with
     * @throws IOException when "writer" throws IOException
     * @throws IllegalArgumentException when the dashboard cannot be serialised
     */
    public void write(Dashboard dashboard, Writer writer) throws IOException {
        try {
            mapper.writeValue(writer, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    /**
     * Writes the pretty printed JSON representation of a dashboard on the given stream.
     * @param dashboard dashboard to be serialised
     * @param stream stream to be written on
     * @throws IOException when "stream" throws IOException
     * @throws IllegalArgumentException when the dashboard cannot be serialised
     */
    public void write(Dashboard dashboard, OutputStream stream) throws IOException {
        try {
            mapper.writeValue(stream, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

}
