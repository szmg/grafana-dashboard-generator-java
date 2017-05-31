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
import uk.co.szmg.grafana.DashboardSerializer;
import uk.co.szmg.grafana.domain.Dashboard;
import uk.co.szmg.grafana.stores.DashboardFilter;
import uk.co.szmg.grafana.stores.DashboardStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Generates JSON resources to the output directory.
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.PREPARE_PACKAGE, requiresDependencyResolution = ResolutionScope.RUNTIME)
public class Generate extends AbstractMojoWithDashboards {

    /**
     * Output directory for the Grafana Json files.
     */
    @Parameter(defaultValue = "${project.build.directory}/grafana-dashboards", required = true)
    protected File outputDirectory;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        List<Dashboard> dashboards = loadDashboards();

        outputDirectory.mkdirs();
        DashboardSerializer dashboardSerializer = new DashboardSerializer();
        for (Dashboard dashboard : dashboards) {
            getLog().info("Generating " + dashboard.getTitle());

            File file = new File(outputDirectory, filenameFor(dashboard));
            try (OutputStream stream = new FileOutputStream(file)) {
                dashboardSerializer.write(dashboard, stream);
            } catch (FileNotFoundException e) {
                throw new MojoFailureException("This should not happen...", e);
            } catch (IOException e) {
                throw new MojoFailureException("Error during writing file: " + file, e);
            }
        }
    }

    private String filenameFor(Dashboard dashboard) {
        return dashboard.getTitle().replaceAll("[\\s:/\\\\]+", "-") + ".json";
    }

}
