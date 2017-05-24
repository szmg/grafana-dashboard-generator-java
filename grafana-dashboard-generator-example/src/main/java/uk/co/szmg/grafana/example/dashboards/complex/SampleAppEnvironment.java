package uk.co.szmg.grafana.example.dashboards.complex;

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

import uk.co.szmg.grafana.dashboard.SimpleEnvironment;

public class SampleAppEnvironment extends SimpleEnvironment {

    private int expectedNumberOfInstances;

    public SampleAppEnvironment(String name, String datasource, String metricsRoot, int expectedNumberOfInstances) {
        super(name, datasource, metricsRoot);
        this.expectedNumberOfInstances = expectedNumberOfInstances;
    }

    public int getExpectedNumberOfInstances() {
        return expectedNumberOfInstances;
    }

    public void setExpectedNumberOfInstances(int expectedNumberOfInstances) {
        this.expectedNumberOfInstances = expectedNumberOfInstances;
    }
}
