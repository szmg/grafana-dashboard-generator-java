package uk.co.szmg.grafana.maven;

/*-
 * #%L
 * grafana-dashboard-maven-plugin
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

import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;
import uk.co.szmg.grafana.domain.Dashboard;
import uk.co.szmg.grafana.stores.DashboardFilter;
import uk.co.szmg.grafana.stores.DashboardStore;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class AbstractMojoWithDashboards extends AbstractMojoWithClasspath {

    @Parameter(required = true)
    protected String rootPackage;

    @Parameter
    protected List<String> includes;

    @Parameter
    protected List<String> excludes;

    @Parameter
    protected List<String> forceInclude;

    protected List<Dashboard> loadDashboards() throws MojoFailureException {
        final DashboardStore dashboardStore = new DashboardStore(rootPackage);
        Thread thread = new Thread("dashboard-loader") {
            @Override
            public void run() {
                dashboardStore.load();
            }
        };

        thread.setContextClassLoader(getClassLoader());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new MojoFailureException("Cannot load dashboards", e);
        }

        // check for duplicates
        Optional<MojoFailureException> duplicationException = dashboardStore
                .getDuplicatesErrorMessage()
                .map(MojoFailureException::new);
        if (duplicationException.isPresent()) {
            throw duplicationException.get();
        }

        // filter
        DashboardFilter dashboardFilter = new DashboardFilter(
                emptyIfNull(forceInclude),
                emptyIfNull(includes),
                emptyIfNull(excludes));
        getLog().debug("all dashboards: " + dashboardStore.getDashboards().size());
        List<Dashboard> dashboards = dashboardFilter.filter(dashboardStore.getDashboards());
        getLog().info("filtered dashboards: " + dashboards.size());
        return dashboards;
    }

    private <E> List<E> emptyIfNull(List<E> list) {
        return list == null ? Collections.emptyList() : list;
    }
}
