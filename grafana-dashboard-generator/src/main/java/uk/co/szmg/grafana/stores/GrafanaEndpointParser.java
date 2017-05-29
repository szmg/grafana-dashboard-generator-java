package uk.co.szmg.grafana.stores;

/*-
 * #%L
 * grafana-dashboard-generator-cli
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import uk.co.szmg.grafana.GrafanaEndpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Example in test/resources/test-endpoints.yaml
 */
public class GrafanaEndpointParser {

    private ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
    private MapType mapOfStringAndEndpointType = mapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, GrafanaEndpoint.class);

    /**
     * Gets a sorted map of endpoint IDs and endpoints.
     *
     * @param input stream that contains the endpoint config
     * @return sorted map of endpoint IDs and endpoints
     * @throws IOException when something wents wrong (e.g., wrong format)
     */
    public Map<String, GrafanaEndpoint> parse(InputStream input) throws IOException {
        return mapper.readerFor(mapOfStringAndEndpointType).readValue(input);
    }

}
