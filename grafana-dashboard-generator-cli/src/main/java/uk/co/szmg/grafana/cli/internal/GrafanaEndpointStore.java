package uk.co.szmg.grafana.cli.internal;

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

import uk.co.szmg.grafana.GrafanaEndpoint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

public abstract class GrafanaEndpointStore {

    private Map<String, GrafanaEndpoint> endpoints;

    public void load() throws IOException {
        try (InputStream stream = getStream()) {
            if (stream != null) {
                endpoints = new GrafanaEndpointParser().parse(stream);
            } else {
                endpoints = Collections.emptyMap();
            }
        }
    }

    public Map<String, GrafanaEndpoint> getEndpoints() {
        return endpoints;
    }

    protected abstract InputStream getStream();

}
