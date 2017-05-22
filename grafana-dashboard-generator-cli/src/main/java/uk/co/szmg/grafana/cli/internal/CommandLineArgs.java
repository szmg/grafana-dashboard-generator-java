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

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import uk.co.szmg.grafana.GrafanaEndpoint;
import uk.co.szmg.grafana.domain.Dashboard;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class CommandLineArgs {

    private String forcedEndpoint;
    private DashboardFilter dashboardFilter;

    public void parse(String[] args, UploaderConfig config) throws IOException {
        OptionParser parser = new OptionParser();
        parser.acceptsAll(asList("B", "batch"), "Non-interactive, batch mode");
        parser.acceptsAll(asList("f", "force"), "Overwrite existing dashboards");
        parser.acceptsAll(asList("d", "dashboard"), "Dashboards to generate; can have multiple value")
                .withRequiredArg()
                .describedAs("title");
        parser.acceptsAll(asList("i", "include"), "Dashboards to include")
                .withRequiredArg()
                .describedAs("regexp");
        parser.acceptsAll(asList("exclude"), "Dashboards to exclude")
                .withRequiredArg()
                .describedAs("regexp");
        parser.accepts("endpoints", "Endpoints YAML file")
                .withRequiredArg()
                .ofType(File.class);
        parser.acceptsAll(asList("e", "endpoint"), "Name of the endpoint to use")
                //.availableUnless("output")
                .withRequiredArg()
                .describedAs("endpoint");
        parser.acceptsAll(asList("o", "output"), "output directory -- will skip upload")
                .availableUnless("endpoint")
                .withRequiredArg()
                .ofType(File.class);
        parser.acceptsAll(asList("h", "?", "help"), "show help" )
                .forHelp();

        OptionSet options = parser.parse(args);

        if (options.has("help")) {
            parser.printHelpOn(System.out);
            throw new ExitPlease(-3);
        }

        config.setBatchMode(options.has("batch"));

        if (options.has("endpoints")) {
            File endpointsFile = (File) options.valueOf("endpoints");
            config.setEndpointStore(new FileBasedGrafanaEndpointStore(endpointsFile));
        }

        if (options.has("force")) {
            config.setOverwrite(true);
        }

        if (options.has("output")) {
            config.setEndpoint(Optional.empty());
            config.setOutputDirectory((File) options.valueOf("output"));
        }

        if (options.has("endpoint")) {
            forcedEndpoint = (String) options.valueOf("endpoint");
        }

        if (options.has("dashboard") || options.has("include") || options.has("exclude"))
        dashboardFilter = new DashboardFilter(
                toStringList(options.valuesOf("dashboard")),
                toStringList(options.valuesOf("include")),
                toStringList(options.valuesOf("exclude")));
    }

    public void apply(UploaderConfig config) {
        if (forcedEndpoint != null) {
            GrafanaEndpoint endpoint = config.getEndpointStore().getEndpoints().get(forcedEndpoint);
            if (endpoint == null) {
                System.err.println("Endpoint not found: " + forcedEndpoint);
                throw new ExitPlease(-5);
            }
            config.setEndpoint(Optional.of(endpoint));
        }

        if (dashboardFilter != null) {
            List<Dashboard> dashboards = dashboardFilter.filter(config.getDashboardStore().getDashboards());
            config.setDashboards(dashboards);
            System.out.println("Dashboards: " + dashboards.stream()
                    .map(Dashboard::getTitle)
                    .collect(Collectors.joining(", ")));
        }
    }

    private List<String> toStringList(List<?> list) {
        return (List<String>) list;
    }


}
