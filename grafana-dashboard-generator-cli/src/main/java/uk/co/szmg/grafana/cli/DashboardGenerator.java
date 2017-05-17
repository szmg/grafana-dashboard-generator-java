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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.co.szmg.grafana.GrafanaEndpoint;
import uk.co.szmg.grafana.domain.Dashboard;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DashboardGenerator {

    private String rootPackage;
    private Optional<GrafanaEndpoint> grafanaEndpoint;

    public DashboardGenerator(String rootPackage, Optional<GrafanaEndpoint> grafanaEndpoint) {
        this.rootPackage = rootPackage;
        this.grafanaEndpoint = grafanaEndpoint;
    }

    public void generateAndUpload() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(rootPackage);
        Map<String, DashboardFactory> factories = applicationContext.getBeansOfType(DashboardFactory.class);

        for (Map.Entry<String, DashboardFactory> entry : factories.entrySet()) {
            List<Dashboard> dashboards = entry.getValue().create();
            System.out.println(entry.getKey());
            System.out.println(dashboards.size());
        }
    }

}
