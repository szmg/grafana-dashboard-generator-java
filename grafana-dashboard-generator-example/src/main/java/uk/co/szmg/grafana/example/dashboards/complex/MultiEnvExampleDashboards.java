package uk.co.szmg.grafana.example.dashboards.complex;

/*-
 * #%L
 * grafana-dashboard-generator-example
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

import uk.co.szmg.grafana.cli.DashboardFactory;
import uk.co.szmg.grafana.dashboard.ColorTheme;
import uk.co.szmg.grafana.domain.Dashboard;
import uk.co.szmg.grafana.domain.Row;
import uk.co.szmg.grafana.domain.SingleStat;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

import static uk.co.szmg.grafana.dashboard.StaticFactories.placeholder;
import static uk.co.szmg.grafana.dashboard.StaticFactories.thinRow;
import static uk.co.szmg.grafana.dashboard.StaticFactories.title;
import static uk.co.szmg.grafana.domain.DomainFactories.newDashboard;
import static uk.co.szmg.grafana.domain.DomainFactories.newRow;

@Named
public class MultiEnvExampleDashboards implements DashboardFactory {

    private SampleAppEnvironment environments[] =
            new SampleAppEnvironment[]{
                    new SampleAppEnvironment("Staging", "graphite", "MYAPP.stage", 2),
                    new SampleAppEnvironment("Production", "aws.graphite", "MYAPP.prod", 10)
            };

    private ColorTheme colorTheme = ColorTheme.RED_YELLOW_GREEN;

    @Override
    public List<Dashboard> create() {
        List<Dashboard> dashboards = new ArrayList<>();
        for (SampleAppEnvironment environment : environments) {
            dashboards.add(create(environment));
        }
        return dashboards;
    }

    private Dashboard create(SampleAppEnvironment environment) {
        XYComponentFactory factory = new XYComponentFactory(environment, colorTheme);

        // might be better off in a factory method
        SingleStat e500 = factory
                .errorCounter("sumSeries({ROOT}.instrumentedFilter.serverErrors.count)", true)
                .withTitle("E500 count");

        // row definitions
        Row row1 = thinRow()
                .addPanel(title("Overview in " + environment.getName()).withSpan(12));

        Row row2 = thinRow()
                .addPanel(e500)
                .addPanel(placeholder(8))
                .addPanel(factory.appInstances());

        Row row3 = newRow()
                .addPanel(factory.responseTime(Resource.USER_REGISTRATION_REQUEST))
                .addPanel(factory.responseTime(Resource.FORGOTTEN_PASSWORD));

        // dashboard
        return newDashboard()
                .withTitle("MYAPP :: " + environment.getName())
                .addTag("generated")
                .addTag("MyApp")
                .addRow(row1)
                .addRow(row2)
                .addRow(row3);
    }

}
