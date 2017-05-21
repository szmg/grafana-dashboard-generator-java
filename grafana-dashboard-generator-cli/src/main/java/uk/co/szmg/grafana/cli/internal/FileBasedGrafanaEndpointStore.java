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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileBasedGrafanaEndpointStore extends GrafanaEndpointStore {

    private File source;

    public FileBasedGrafanaEndpointStore(File source) {
        this.source = source;
    }

    @Override
    protected InputStream getStream() {
        try {
            return new FileInputStream(source);
        } catch (FileNotFoundException e) {
            System.err.println("Endpoint config file is not found: " + source);
            throw new ExitPlease(-4);
        }
    }
}
