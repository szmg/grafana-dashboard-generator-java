package uk.co.szmg.grafana.cli;

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

import org.fusesource.jansi.AnsiConsole;
import uk.co.szmg.grafana.DashboardSerializer;
import uk.co.szmg.grafana.DashboardUploader;
import uk.co.szmg.grafana.GrafanaClient;
import uk.co.szmg.grafana.cli.internal.ClasspathGrafanaEndpointStore;
import uk.co.szmg.grafana.cli.internal.CommandLineArgs;
import uk.co.szmg.grafana.cli.internal.DashboardStore;
import uk.co.szmg.grafana.cli.internal.ExitPlease;
import uk.co.szmg.grafana.cli.internal.GrafanaEndpointStore;
import uk.co.szmg.grafana.cli.internal.Interaction;
import uk.co.szmg.grafana.cli.internal.UploaderConfig;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.fusesource.jansi.Ansi.ansi;

public class DashboardGeneratorApplication {

    private String rootPackage;

    public DashboardGeneratorApplication(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public int main(String[] args) throws IOException {
        try {
            return safeMain(args);
        } catch (ExitPlease ex) {
            return ex.getReturnCode();
        }
    }

    private int safeMain(String[] args) throws IOException, ExitPlease {
        AnsiConsole.systemInstall();

        // welcome
        System.out.println(ansi().render("\n@|blue Welcome to Grafana Dashboard Builder|@\n"));

        UploaderConfig config = new UploaderConfig();

        CommandLineArgs commandLineArgs = new CommandLineArgs();
        commandLineArgs.parse(args, config);
        initStores(config);
        commandLineArgs.apply(config);
        loadDefaults(config);

        Interaction.askUserForDetails(config);

        if (config.getEndpoint().isPresent()) {
            upload(config);
        } else {
            writeToFile(config);
        }

        return 0;
    }

    private void initStores(UploaderConfig config) {
        // dashboard
        config.setDashboardStore(new DashboardStore(rootPackage));
        DashboardStore dashboardStore = config.getDashboardStore();
        dashboardStore.load();

        // check duplicates
        Collection<String> duplicates = dashboardStore.getDuplicates();
        if (!duplicates.isEmpty()) {
            System.err.printf("%d duplicates found among dashboards:", duplicates.size());
            for (String duplicate : duplicates) {
                System.err.println("  " + duplicate);
            }
            throw new ExitPlease(-1);
        }

        // endpoint
        GrafanaEndpointStore endpointStore = config.getEndpointStore();
        if (endpointStore == null) {
            endpointStore = new ClasspathGrafanaEndpointStore("/endpoints.yaml");
            config.setEndpointStore(endpointStore);
        }

        try {
            endpointStore.load();
        } catch (IOException e) {
            System.err.println("Could not load endpoint store; " + e.getMessage());
            throw new ExitPlease(-2);
        }

    }

    private void loadDefaults(UploaderConfig config) {
        List<Dashboard> dashboards = config.getDashboardStore().getDashboards();
        if (config.getDashboards() == null && dashboards.size() == 1) {
            config.setDashboards(dashboards);
        }

        if (config.getEndpointStore().getEndpoints().isEmpty()) {
            config.setEndpoint(Optional.empty());
        }

        if (config.getOutputDirectory() == null) {
            config.setOutputDirectory(new File("./output"));
        }

        if (config.isBatchMode() && config.getOverwrite() == null) {
            config.setOverwrite(false);
        }
    }

    private void upload(UploaderConfig config) {
        boolean overwrite = config.getOverwrite();
        DashboardUploader dashboardUploader = new DashboardUploader(config.getEndpoint().get());
        for (Dashboard dashboard : config.getDashboards()) {
            System.out.printf("%s: ", dashboard.getTitle());

            try {
                dashboardUploader.upload(dashboard, overwrite);
                System.out.println(ansi().render("@|green done|@"));
            } catch (RuntimeException e) {
                boolean alreadyExists = false;
                if (e.getCause() instanceof GrafanaClient.UnexpectedGrafanaResponseException) {
                    GrafanaClient.UnexpectedGrafanaResponseException ex = (GrafanaClient.UnexpectedGrafanaResponseException) e.getCause();
                    alreadyExists = ex.getResponseCode() == 412;
                }

                if (alreadyExists && !overwrite) {
                    System.out.println(ansi().render("@|red already exists|@"));
                } else {
                    throw e;
                }
            }
        }
    }

    private void writeToFile(UploaderConfig config) throws IOException {
        DashboardSerializer dashboardSerializer = new DashboardSerializer();
        File outputDirectory = config.getOutputDirectory();
        outputDirectory.mkdirs();
        for (Dashboard dashboard : config.getDashboards()) {
            System.out.printf("%s: ", dashboard.getTitle());

            File f = new File(outputDirectory, filenameFor(dashboard));
            if (f.exists() && !config.getOverwrite()) {
                System.out.println(ansi().render("@|red already exists|@"));
            } else {
                try (FileOutputStream stream = new FileOutputStream(f)) {
                    dashboardSerializer.write(dashboard, stream);
                }
                System.out.println(ansi().render("@|green done|@"));
            }

        }

    }

    private String filenameFor(Dashboard dashboard) {
        return dashboard.getTitle().replaceAll("[\\s:/\\\\]+", "-") + ".json";
    }

}
