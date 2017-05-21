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
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class UploaderConfig {

    private boolean batchMode;
    private DashboardStore dashboardStore;
    private GrafanaEndpointStore endpointStore;
    private Boolean overwrite;
    private Optional<GrafanaEndpoint> endpoint;
    private List<Dashboard> dashboards;
    private File outputDirectory;

    public boolean isBatchMode() {
        return batchMode;
    }

    public void setBatchMode(boolean batchMode) {
        this.batchMode = batchMode;
    }

    public DashboardStore getDashboardStore() {
        return dashboardStore;
    }

    public void setDashboardStore(DashboardStore dashboardStore) {
        this.dashboardStore = dashboardStore;
    }

    public GrafanaEndpointStore getEndpointStore() {
        return endpointStore;
    }

    public void setEndpointStore(GrafanaEndpointStore endpointStore) {
        this.endpointStore = endpointStore;
    }

    public Boolean getOverwrite() {
        return overwrite;
    }

    public void setOverwrite(Boolean overwrite) {
        this.overwrite = overwrite;
    }

    public Optional<GrafanaEndpoint> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(Optional<GrafanaEndpoint> endpoint) {
        this.endpoint = endpoint;
    }

    public List<Dashboard> getDashboards() {
        return dashboards;
    }

    public void setDashboards(List<Dashboard> dashboards) {
        this.dashboards = dashboards;
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(File outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

}
