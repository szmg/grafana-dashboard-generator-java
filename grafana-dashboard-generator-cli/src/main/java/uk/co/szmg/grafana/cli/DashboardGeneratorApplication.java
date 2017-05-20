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

import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.prompt.CheckboxResult;
import de.codeshelf.consoleui.prompt.ConfirmResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.CheckboxPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import org.fusesource.jansi.AnsiConsole;
import uk.co.szmg.grafana.DashboardSerializer;
import uk.co.szmg.grafana.DashboardUploader;
import uk.co.szmg.grafana.GrafanaClient;
import uk.co.szmg.grafana.GrafanaEndpoint;
import uk.co.szmg.grafana.cli.internal.ClasspathGrafanaEndpointStore;
import uk.co.szmg.grafana.cli.internal.DashboardStore;
import uk.co.szmg.grafana.cli.internal.GrafanaEndpointStore;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.fusesource.jansi.Ansi.ansi;

public class DashboardGeneratorApplication {

    private DashboardStore dashboardStore;

    public DashboardGeneratorApplication(String rootPackage) {
        dashboardStore = new DashboardStore(rootPackage);
    }

    public int main(String[] args) throws IOException {
        AnsiConsole.systemInstall();

        dashboardStore.load();

        // welcome
        System.out.println(ansi().render("\n@|blue Welcome to Grafana Dashboard Builder|@\n"));

        // check duplicates
        Collection<String> duplicates = dashboardStore.getDuplicates();
        if (!duplicates.isEmpty()) {
            System.err.printf("%d duplicates found among dashboards:", duplicates.size());
            for (String duplicate : duplicates) {
                System.err.println("  " + duplicate);
            }
            return -1;
        }

        // dashboards
        List<Dashboard> selectedDashboards = null;
        List<Dashboard> dashboards = dashboardStore.getDashboards();
        if (dashboards.size() == 1) {
            selectedDashboards = dashboards;
        }

        // endpoints
        GrafanaEndpointStore endpointStore = new ClasspathGrafanaEndpointStore("/endpoints.yaml");
        endpointStore.load();

        Optional<GrafanaEndpoint> endpoint = null;
        Map<String, GrafanaEndpoint> endpoints = endpointStore.getEndpoints();
        if (endpoints.isEmpty()) {
            endpoint = Optional.empty();
        } else if (endpoints.size() == 1) {
            endpoint = Optional.of(endpoints.values().iterator().next());
        }

        // Ask stuff
        ConsolePrompt prompt = new ConsolePrompt();

        Map<String, ? extends PromtResultItemIF> results;
        do {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            if (selectedDashboards == null) {
                CheckboxPromptBuilder dashboardPrompt = promptBuilder.createCheckboxPrompt()
                        .name("dashboards")
                        .message("Which dashboard(s) would you like to work with?");
                for (int i = 0; i < dashboards.size(); i++) {
                    dashboardPrompt.newItem(Integer.toString(i)).text(dashboards.get(i).getTitle()).check().add();
                }
                dashboardPrompt.addPrompt();
            }

            if (endpoint == null) {
                ListPromptBuilder endpointPrompt = promptBuilder.createListPrompt()
                        .name("endpoint")
                        .message("Which endpoint to upload to?");
                for (Map.Entry<String, GrafanaEndpoint> entry : endpoints.entrySet()) {
                    endpointPrompt
                            .newItem(entry.getKey())
                            .text(String.format("%s (%s)", entry.getKey(), entry.getValue().getBaseUrl()))
                            .add();
                }
                endpointPrompt
                        .newItem("-")
                        .text("Just generate the files to ./output")
                        .add()
                        .addPrompt();
            }

            promptBuilder.createConfirmPromp()
                    .name("overwrite")
                    .message("Should dashboards be overwritten if already exist?")
                    .defaultValue(ConfirmChoice.ConfirmationValue.YES)
                    .addPrompt();

            promptBuilder.createConfirmPromp()
                    .name("confirmed")
                    .message("Are you ready?")
                    .defaultValue(ConfirmChoice.ConfirmationValue.YES)
                    .addPrompt();

            results = prompt.prompt(promptBuilder.build());
        } while (!getBooleanResult(results, "confirmed"));

        if (results.containsKey("dashboards")) {
            CheckboxResult selection = (CheckboxResult) results.get("dashboards");
            selectedDashboards = new ArrayList<>();
            for (String i : selection.getSelectedIds()) {
                selectedDashboards.add(dashboards.get(Integer.parseInt(i, 10)));
            }
        }

        if (results.containsKey("endpoint")) {
            ListResult result = (ListResult) results.get("endpoint");
            if ("-".equals(result.getSelectedId())) {
                endpoint = Optional.empty();
            } else {
                endpoint = Optional.of(endpoints.get(result.getSelectedId()));
            }
        }

        boolean overwrite = getBooleanResult(results, "overwrite");

        if (endpoint.isPresent()) {
            upload(selectedDashboards, endpoint.get(), overwrite);
        } else {
            writeToFile(selectedDashboards, new File("./output"), overwrite);
        }

        return 0;
    }

    private boolean getBooleanResult(Map<String, ? extends PromtResultItemIF> results, String key) {
        return ((ConfirmResult) results.get(key)).getConfirmed() == ConfirmChoice.ConfirmationValue.YES;
    }

    private void upload(List<Dashboard> selectedDashboards, GrafanaEndpoint grafanaEndpoint, boolean overwrite) {
        DashboardUploader dashboardUploader = new DashboardUploader(grafanaEndpoint);
        for (Dashboard dashboard : selectedDashboards) {
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

    private void writeToFile(List<Dashboard> selectedDashboards, File outputDir, boolean overwrite) throws IOException {
        DashboardSerializer dashboardSerializer = new DashboardSerializer();
        outputDir.mkdirs();
        for (Dashboard dashboard : selectedDashboards) {
            System.out.printf("%s: ", dashboard.getTitle());

            File f = new File(outputDir, filenameFor(dashboard));
            if (f.exists() && !overwrite) {
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
