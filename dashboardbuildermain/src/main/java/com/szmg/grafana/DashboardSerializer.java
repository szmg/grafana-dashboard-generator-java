package com.szmg.grafana;

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
import com.szmg.grafana.domain.Dashboard;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

public class DashboardSerializer {

    private final ObjectMapper mapper;

    public DashboardSerializer() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String toString(Dashboard dashboard) {
        try {
            return mapper.writeValueAsString(dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    public void write(Dashboard dashboard, Writer writer) throws IOException {
        try {
            mapper.writeValue(writer, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    public void write(Dashboard dashboard, OutputStream stream) throws IOException {
        try {
            mapper.writeValue(stream, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

}
