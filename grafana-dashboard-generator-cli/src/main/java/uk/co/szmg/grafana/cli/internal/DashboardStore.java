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

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import uk.co.szmg.grafana.cli.DashboardFactory;
import uk.co.szmg.grafana.domain.Dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Collections.unmodifiableList;

public class DashboardStore {

    private String rootPackage;

    private List<Dashboard> dashboards;

    public DashboardStore(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    public void load() {
        List<Dashboard> dashboards = new ArrayList<>();
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(rootPackage);
        Map<String, DashboardFactory> factories = applicationContext.getBeansOfType(DashboardFactory.class);

        for (DashboardFactory dashboardFactory : factories.values()) {
            dashboards.addAll(dashboardFactory.create());
        }

        Map<String, Dashboard> dashboardBeans = applicationContext.getBeansOfType(Dashboard.class);
        dashboards.addAll(dashboardBeans.values());

        this.dashboards = unmodifiableList(dashboards);
    }

    public Collection<String> getDuplicates() {
        Set<String> all = new HashSet<>();
        Set<String> dupes = new HashSet<>();

        for (Dashboard dashboard : dashboards) {
            String title = dashboard.getTitle();
            if (all.contains(title)) {
                dupes.add(title);
            } else {
                all.add(title);
            }
        }

        return dupes;
    }

    public List<Dashboard> getDashboards() {
        return dashboards;
    }
}
