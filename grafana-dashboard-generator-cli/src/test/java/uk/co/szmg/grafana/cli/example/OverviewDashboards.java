package uk.co.szmg.grafana.cli.example;

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

import uk.co.szmg.grafana.cli.DashboardFactory;
import uk.co.szmg.grafana.domain.Dashboard;

import javax.inject.Named;
import java.util.Collections;
import java.util.List;

import static uk.co.szmg.grafana.domain.DomainFactories.newDashboard;
import static uk.co.szmg.grafana.domain.DomainFactories.newRow;
import static uk.co.szmg.grafana.domain.DomainFactories.newText;

@Named
public class OverviewDashboards implements DashboardFactory {

    @Override
    public List<Dashboard> create() {
        Dashboard dashboard = newDashboard()
                .withTitle("Overview dashboard")
                .addRow(newRow()
                        .addPanel(newText().withSpan(12).withTransparent(true).withContent("Hello World!")));
        return Collections.singletonList(dashboard);
    }

}
