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

import uk.co.szmg.grafana.domain.Dashboard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

/**
 * Filters dashboards based on their titles.
 *
 * <p>It's a three step filter, starting from an empty set:
 * <ol>
 *     <li>adds every dashboard with title matching any of {@code includeFilters}
 *     <li>removes every dashboard with title matching any of {@code excludeFilters};
 *     if {@code includeFilters} is empty, it adds all dashboards first
 *     <li>adds all dashboards specified in {@code forceInclude}
 * </ol>
 *
 * <p>Note that {@code includeFilters} and {@code excludeFilters} looks for
 * partial match and are regular expressions, while {@code forceInclude}
 * uses String.equals to decide if a dashboard title matches.
 */
public class DashboardFilter {

    private List<String> forceInclude;
    private List<String> includeFilters;
    private List<String> excludeFilters;

    /**
     * Creates a DashboardFilter.
     *
     * @param forceInclude list of dashboard titles to include, no matter what
     * @param includeFilters list of regexps to include dashboards
     * @param excludeFilters list of regexps to exclude dashboards
     */
    public DashboardFilter(List<String> forceInclude, List<String> includeFilters, List<String> excludeFilters) {
        this.forceInclude = forceInclude;
        this.includeFilters = includeFilters;
        this.excludeFilters = excludeFilters;
    }

    /**
     * Do the filtering. For the filter method, see {@link DashboardFilter}.
     * @param dashboards dashboards to filter
     * @return set of dashboards that mathced the filter
     * @throws IncludedDashboardIsNotFound when a dashboard in {@code forceInclude} is not found
     */
    public List<Dashboard> filter(List<Dashboard> dashboards) {
        Set<Dashboard> result = new LinkedHashSet<>();

        for (String includeFilter : includeFilters) {
            add(result, includeFilter, dashboards);
        }

        if (includeFilters.isEmpty() && !excludeFilters.isEmpty()) {
            result.addAll(dashboards);
        }

        for (String excludeFilter : excludeFilters) {
            remove(result, excludeFilter);
        }

        if (!forceInclude.isEmpty()) {
            Map<String, Dashboard> dashboardIndex = dashboards.stream()
                    .collect(toMap(Dashboard::getTitle, identity()));
            for (String s : forceInclude) {
                Dashboard dashboard = dashboardIndex.get(s);
                if (dashboard == null) {
                    throw new IncludedDashboardIsNotFound(s);
                }
                result.add(dashboard);
            }
        }

        return new ArrayList<>(result);
    }

    private void remove(Set<Dashboard> result, String filter) {
        Pattern pattern = Pattern.compile(filter);
        Iterator<Dashboard> it = result.iterator();
        while (it.hasNext()) {
            if (pattern.matcher(it.next().getTitle()).find()) {
                it.remove();
            }
        }
    }

    private void add(Set<Dashboard> result, String filter, List<Dashboard> dashboards) {
        Pattern pattern = Pattern.compile(filter);
        for (Dashboard dashboard : dashboards) {
            if (pattern.matcher(dashboard.getTitle()).find()) {
                result.add(dashboard);
            }
        }
    }

    /**
     * Exception showing that a dashboard that is explicitly included
     * with {@code forceInclude} is not among all dashboards.
     *
     * @see DashboardFilter
     */
    public static class IncludedDashboardIsNotFound extends IllegalArgumentException {

        private String dashboardTitle;

        /**
         * Creates an IncludedDashboardIsNotFound.
         * @param dashboardTitle title of the dashboard that could not be found
         */
        public IncludedDashboardIsNotFound(String dashboardTitle) {
            super("Included dashboard cannot be found: " + dashboardTitle);
            this.dashboardTitle = dashboardTitle;
        }

        /**
         * Gets dashboard title that could not be found.
         * @return dashboard title
         */
        public String getDashboardTitle() {
            return dashboardTitle;
        }
    }
}
