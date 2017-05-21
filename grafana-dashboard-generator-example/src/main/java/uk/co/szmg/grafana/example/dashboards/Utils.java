package uk.co.szmg.grafana.example.dashboards;

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

import uk.co.szmg.grafana.domain.Dashboard;

import static uk.co.szmg.grafana.domain.DomainFactories.newAlertAnnotation;
import static uk.co.szmg.grafana.domain.DomainFactories.newAnnotationList;
import static uk.co.szmg.grafana.domain.DomainFactories.newDashboard;
import static uk.co.szmg.grafana.domain.DomainFactories.newGraphiteAnnotation;
import static uk.co.szmg.grafana.domain.DomainFactories.newRow;
import static uk.co.szmg.grafana.domain.DomainFactories.newText;

public class Utils {

    public static Dashboard createSimpleDashboard(String title, Class<?> clazz) {
        return newDashboard()
                .withTitle(title)
                .addRow(newRow()
                        .addPanel(newText()
                                .withSpan(12).withContent("This was created by " + clazz.getSimpleName())))
                .withAnnotations(newAnnotationList()
                        .addList(newAlertAnnotation()
                                .withName("alert")
                                .withLimit(300)
                                .withEnable(true))
                        .addList(newGraphiteAnnotation()
                                .withName("deployment")
                                .withDatasource("graphite")
                                .withTarget("app.deployment")
                                .withIconColor("green")));
    }

}
