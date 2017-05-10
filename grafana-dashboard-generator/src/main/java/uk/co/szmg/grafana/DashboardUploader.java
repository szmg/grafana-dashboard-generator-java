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

import uk.co.szmg.grafana.domain.Dashboard;

/**
 * Serialises and uploads a dashboard to a Grafana endpoint.
 *
 * This class uses {@link DashboardSerializer#toString(Dashboard)} and
 * {@link GrafanaClient#uploadDashboard(String, boolean)} method.
 */
public class DashboardUploader {

    private GrafanaClient client;
    private DashboardSerializer dashboardSerializer;

    /**
     * Constructor that sets the Grafana endpoint. It also creates the client,
     * but no connection is opened at this point.
     *
     * @param endpoint Grafana endpoint to be used
     */
    public DashboardUploader(GrafanaEndpoint endpoint) {
        client = new GrafanaClient(endpoint);
        dashboardSerializer = new DashboardSerializer();
    }

    /**
     * Serializes and uploads a dashboard.
     *
     * Throws exceptions if anything is wrong.
     *
     * @param dashboard dashboard to upload
     * @param overwrite should anything be overwritten?
     */
    public void upload(Dashboard dashboard, boolean overwrite) {
        if (dashboard.getTitle() == null) {
            throw new IllegalArgumentException("Dashboard must have a title.");
        }

        String dashboardJson = dashboardSerializer.toString(dashboard);
        try {
            client.uploadDashboard(dashboardJson, overwrite);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Error happened during uploading [%s]", dashboard.getTitle()), e);
        }
    }
}
