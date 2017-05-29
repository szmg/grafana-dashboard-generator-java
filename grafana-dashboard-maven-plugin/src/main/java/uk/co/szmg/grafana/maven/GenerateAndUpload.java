package uk.co.szmg.grafana.maven;

/*-
 * #%L
 * grafana-dashboard-maven-plugin
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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import uk.co.szmg.grafana.DashboardUploader;
import uk.co.szmg.grafana.GrafanaClient;
import uk.co.szmg.grafana.GrafanaEndpoint;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.IOException;
import java.util.List;

@Mojo(name = "generateAndUpload", defaultPhase = LifecyclePhase.DEPLOY, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class GenerateAndUpload extends AbstractMojoWithDashboards {

    @Parameter(property = "grafana.overwrite", defaultValue = "false", required = true)
    protected boolean overwrite;

    @Parameter(property = "grafana.url", required = true)
    protected String url;

    @Parameter(property = "grafana.sessionCookie")
    protected String sessionCookie;

    @Parameter(property = "grafana.apiKey")
    protected String apiKey;

    @Parameter(property = "grafana.skipSSLValidation", defaultValue = "false", required = true)
    protected boolean skipSSLValidation;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        GrafanaEndpoint endpoint = new GrafanaEndpoint();
        endpoint.setBaseUrl(url);
        endpoint.setSessionCookie(sessionCookie);
        endpoint.setApiKey(apiKey);
        endpoint.setSkipSSLValidation(skipSSLValidation);

        List<Dashboard> dashboards = loadDashboards();

        DashboardUploader uploader = new DashboardUploader(endpoint);
        for (Dashboard dashboard : dashboards) {
            String title = dashboard.getTitle();
            try {
                getLog().debug(title + " - starting upload");
                uploader.upload(dashboard, overwrite);
                getLog().info(title + " - uploaded");
            } catch (GrafanaClient.UnexpectedGrafanaResponseException e) {
                if (!overwrite && e.getResponseCode() == 412) {
                    getLog().info(title + " - dashboard already exists, skipping");
                } else {
                    throw new MojoFailureException("Unexpected Grafana response while uploading " + title, e);
                }
            } catch (RuntimeException e) {
                throw new MojoFailureException("Error during uploading " + title, e);
            }
        }
    }
}
