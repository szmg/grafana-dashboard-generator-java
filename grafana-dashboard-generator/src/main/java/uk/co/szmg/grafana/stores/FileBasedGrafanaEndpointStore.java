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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Endpoint store that reads from file.
 */
public class FileBasedGrafanaEndpointStore extends GrafanaEndpointStore {

    private File source;

    /**
     * Creates a FileBasedGrafanaEndpointStore.
     * @param source path of the endpoint config yaml file
     */
    public FileBasedGrafanaEndpointStore(File source) {
        this.source = source;
    }

    @Override
    protected InputStream getStream() throws FileNotFoundException {
        return new FileInputStream(source);
    }
}
