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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.FileUtils;
import uk.co.szmg.grafana.GrafanaClient;
import uk.co.szmg.grafana.GrafanaEndpoint;

import java.io.File;
import java.io.IOException;

@Mojo(name = "upload", defaultPhase = LifecyclePhase.DEPLOY)
public class Upload extends AbstractMojo {

    @Parameter(property = "grafana.sourceDir", defaultValue = "${project.build.directory}/grafana-dashboards", required = true)
    protected File inputDirectory;

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

        GrafanaClient client = new GrafanaClient(endpoint);

        getLog().info("Uploading to " + url);

        for (File file : inputDirectory.listFiles()) {
            String fileName = file.getName();
            if (fileName.endsWith(".json")) {
                String json;
                try {
                    json = FileUtils.fileRead(file);
                } catch (IOException e) {
                    throw new MojoFailureException("Error during reading " + file, e);
                }

                try {
                    getLog().debug(fileName + " - starting upload");
                    client.uploadDashboard(json, overwrite);
                    getLog().info(fileName + " - uploaded");
                } catch (GrafanaClient.UnexpectedGrafanaResponseException e) {
                    if (!overwrite && e.getResponseCode() == 412) {
                        getLog().info(fileName + " - dashboard already exists, skipping");
                    } else {
                        throw new MojoFailureException("Unexpected Grafana response while uploading " + fileName, e);
                    }
                } catch (IOException e) {
                    throw new MojoFailureException("Error during uploading " + fileName, e);
                }
            }
        }

    }
}
