package uk.co.szmg.grafana.dashboard;

/*-
 * #%L
 * grafana-dashboard-generator
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

/**
 * An abstraction of the environment that the dashboard
 * is going to monitor.
 *
 * For example staging and production might have different dashboard instances,
 * but they should use the same "template".
 *
 * This interface is used by {@link BasicFactories} and its
 * descendants.
 *
 * It has a basic, Java Bean implementation in {@link SimpleEnvironment}.
 *
 * @see BasicFactories
 * @see SimpleEnvironment
 * @since 1.1
 */
public interface Environment {

    /**
     * Gets the name of the environment, e.g., production, staging.
     * @return name of the environment
     */
    String getName();

    /**
     * Gets the default data source.
     * @return default data source
     */
    String getDatasource();

    /**
     * Gets the metrics root that {@code {ROOT}} will be replaced with in target queries.
     *
     * <p>Use this when your metrics have different prefixes for each environment, e.g.
     * {@code SUPERAPP.prod.instance1.someResource.GET.count}.
     *
     * @return metrics root
     */
    String getMetricsRoot();

}
