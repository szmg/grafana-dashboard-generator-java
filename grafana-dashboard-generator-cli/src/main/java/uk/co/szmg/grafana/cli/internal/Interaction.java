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

import de.codeshelf.consoleui.elements.ConfirmChoice;
import de.codeshelf.consoleui.elements.PromptableElementIF;
import de.codeshelf.consoleui.prompt.CheckboxResult;
import de.codeshelf.consoleui.prompt.ConfirmResult;
import de.codeshelf.consoleui.prompt.ConsolePrompt;
import de.codeshelf.consoleui.prompt.ListResult;
import de.codeshelf.consoleui.prompt.PromtResultItemIF;
import de.codeshelf.consoleui.prompt.builder.CheckboxPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.ListPromptBuilder;
import de.codeshelf.consoleui.prompt.builder.PromptBuilder;
import uk.co.szmg.grafana.GrafanaEndpoint;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Interaction {

    public static void askUserForDetails(UploaderConfig config) throws IOException {
        List<Dashboard> dashboards = config.getDashboardStore().getDashboards();
        Map<String, GrafanaEndpoint> endpoints = config.getEndpointStore().getEndpoints();

        ConsolePrompt prompt = new ConsolePrompt();

        Map<String, ? extends PromtResultItemIF> results = Collections.emptyMap();
        do {
            PromptBuilder promptBuilder = prompt.getPromptBuilder();

            if (config.getDashboards() == null) {
                CheckboxPromptBuilder dashboardPrompt = promptBuilder.createCheckboxPrompt()
                        .name("dashboards")
                        .message("Which dashboard(s) would you like to work with?");
                for (int i = 0; i < dashboards.size(); i++) {
                    dashboardPrompt.newItem(Integer.toString(i)).text(dashboards.get(i).getTitle()).check().add();
                }
                dashboardPrompt.addPrompt();
            }

            if (config.getEndpoint() == null) {
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

            if (config.getOverwrite() == null) {
                promptBuilder.createConfirmPromp()
                        .name("overwrite")
                        .message("Should dashboards be overwritten if already exist?")
                        .defaultValue(ConfirmChoice.ConfirmationValue.YES)
                        .addPrompt();
            }

            promptBuilder.createConfirmPromp()
                    .name("confirmed")
                    .message("Are you ready?")
                    .defaultValue(ConfirmChoice.ConfirmationValue.YES)
                    .addPrompt();


            List<PromptableElementIF> questions = promptBuilder.build();
            if (questions.size() == 1) {
                // only the confirmation, we know everything
                break;
            }
            results = prompt.prompt(questions);
        } while (!getBooleanResult(results, "confirmed"));

        if (results.containsKey("dashboards")) {
            CheckboxResult selection = (CheckboxResult) results.get("dashboards");
            List<Dashboard> selectedDashboards = new ArrayList<>();
            for (String i : selection.getSelectedIds()) {
                selectedDashboards.add(dashboards.get(Integer.parseInt(i, 10)));
            }
            config.setDashboards(selectedDashboards);
        }

        if (results.containsKey("endpoint")) {
            ListResult result = (ListResult) results.get("endpoint");
            if ("-".equals(result.getSelectedId())) {
                config.setEndpoint(Optional.empty());
            } else {
                config.setEndpoint(Optional.of(endpoints.get(result.getSelectedId())));
            }
        }

        if (results.containsKey("overwrite")) {
            config.setOverwrite(getBooleanResult(results, "overwrite"));
        }
    }

    private static boolean getBooleanResult(Map<String, ? extends PromtResultItemIF> results, String key) {
        return ((ConfirmResult) results.get(key)).getConfirmed() == ConfirmChoice.ConfirmationValue.YES;
    }
}
