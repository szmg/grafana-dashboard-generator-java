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

/**
 * Provides dashboard loading and filtering support for a mojo.
 */
public abstract class AbstractMojoWithDashboards extends AbstractMojoWithClasspath {

    /**
     * Root package for the Spring context in which {@link Dashboard}s and
     * {@link uk.co.szmg.grafana.DashboardFactory}s should be looked up.
     */
    @Parameter(required = true)
    protected String rootPackage;

    /**
     * Dashboard title regexp to be included.
     *
     * <p>If empty and {@code excludes} is not empty, it defaults
     * to all dashboards.
     */
    @Parameter
    protected List<String> includes;

    /**
     * Dashboard title regexp to be excluded.
     *
     * <p>Filtering is done after the {@code includes} filter
     * has been applied. If that is empty, filtering happens
     * on all dashboards.
     */
    @Parameter
    protected List<String> excludes;

    /**
     * Exact dashboard titles to explicitly include.
     *
     * <p>This will add any matching dashboards after the filtering.
     * This means that a dashboard can be excluded, but if it is added
     * to {@code forceInclude}, it will be taken into account.
     *
     * <p>Providing not existing dashboard title will result
     * in BUILD FAILURE.
     */
    @Parameter
    protected List<String> forceInclude;

    /**
     * Loads, filters and gets dashboards.
     * @return list of dashboards
     * @throws MojoFailureException when dashboards cannot be found
     * @throws MojoFailureException when there are more dashboard with the same title
     */
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
