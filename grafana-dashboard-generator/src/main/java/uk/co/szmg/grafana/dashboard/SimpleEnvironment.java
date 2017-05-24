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
 * Simple Java Bean implementation of {@link Environment}.
 *
 * One can use this for simple projects or either extend this
 * or implement.
 *
 * @see Environment
 * @since 1.1
 */
public class SimpleEnvironment implements Environment {

    private String name;
    private String datasource;
    private String metricsRoot;

    /**
     * Default constructor.
     */
    public SimpleEnvironment() {
    }

    /**
     * Convenience constructor.
     *
     * @param name name of the environment, e.g., production
     * @param datasource default datasource for that environment
     * @param metricsRoot default metrics root; {@code {ROOT}} in targets will be substituted to this value
     */
    public SimpleEnvironment(String name, String datasource, String metricsRoot) {
        this.name = name;
        this.datasource = datasource;
        this.metricsRoot = metricsRoot;
    }

    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name name of the environment, e.g., production
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDatasource() {
        return datasource;
    }

    /**
     * Sets default datasource.
     *
     * @param datasource default datasource for that environment
     */
    public void setDatasource(String datasource) {
        this.datasource = datasource;
    }

    @Override
    public String getMetricsRoot() {
        return metricsRoot;
    }

    /**
     * Sets metrics root, that {@code {ROOT}} will be substituted with in targets.
     *
     * @param metricsRoot default metrics root
     */
    public void setMetricsRoot(String metricsRoot) {
        this.metricsRoot = metricsRoot;
    }
}
