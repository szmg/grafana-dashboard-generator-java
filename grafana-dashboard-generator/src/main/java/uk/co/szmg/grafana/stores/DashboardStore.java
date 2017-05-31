package uk.co.szmg.grafana.stores;

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
import uk.co.szmg.grafana.DashboardFactory;
import uk.co.szmg.grafana.domain.Dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableList;

/**
 * Loads and provides dashboards.
 *
 * <p>First, an annotation based Spring context is built
 * in {@code rootPackage}. Then every {@link DashboardFactory}
 * beans are looked up and invoked to create dashboards.
 * Finally, all {@link Dashboard} beans are read.
 *
 * <p>This class can also return the duplicated dashboards.
 * Two dashboards are duplicated if their titles are equal.
 */
public class DashboardStore {

    private String rootPackage;

    private List<Dashboard> dashboards;

    /**
     * Creates a DashboardStore.
     * @param rootPackage root package of the annotation based spring context to be searched
     */
    public DashboardStore(String rootPackage) {
        this.rootPackage = rootPackage;
    }

    /**
     * Loads dashboards.
     *
     * <p>This will create the Spring context and look for
     * {@link DashboardFactory} and {@link Dashboard} implementations.
     */
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

    /**
     * Gets a list of dashboard titles that presents
     * more than once among the dashboards.
     *
     * <p>Grafana treats them as equal, so there is no point
     * uploading them twice.
     *
     * @return list of dashboard titles
     */
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

    /**
     * Gets an error message with the list of duplicated dashboards if any.
     * @return an error message if there are duplicates, otherwise {@code Optional.empty()}
     */
    public Optional<String> getDuplicatesErrorMessage() {
        Collection<String> duplicates = getDuplicates();
        String message = null;
        if (!duplicates.isEmpty()) {
            message = String.format("%d duplicates found among dashboards:", duplicates.size());

            message += duplicates.stream().collect(Collectors.joining(", "));
        }
        return Optional.ofNullable(message);
    }

    /**
     * Gets all dashboards.
     * @return list of dashboards
     */
    public List<Dashboard> getDashboards() {
        return dashboards;
    }
}
