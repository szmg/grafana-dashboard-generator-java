package com.szmg.grafana;

import com.szmg.grafana.domain.Dashboard;

import java.io.IOException;

public class DashboardUploader {

    private GrafanaClient client;
    private DashboardSerializer dashboardSerializer;

    public DashboardUploader(GrafanaEndpoint endpoint) {
        client = new GrafanaClient(endpoint);
        dashboardSerializer = new DashboardSerializer();
    }

    /**
     * Serializes and uploads a dashboard.
     *
     * Throws exceptions if everything's not fine.
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
