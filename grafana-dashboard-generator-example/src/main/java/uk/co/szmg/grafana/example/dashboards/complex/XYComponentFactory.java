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

import uk.co.szmg.grafana.dashboard.BasicFactories;
import uk.co.szmg.grafana.dashboard.ColorTheme;
import uk.co.szmg.grafana.domain.Graph;
import uk.co.szmg.grafana.domain.SingleStat;

public class XYComponentFactory extends BasicFactories<SampleAppEnvironment> {

    /**
     * Constructor.
     *
     * @param environment environment that this factory should work for
     * @param colorTheme  color theme
     */
    public XYComponentFactory(SampleAppEnvironment environment, ColorTheme colorTheme) {
        super(environment, colorTheme);
    }

    public Graph responseTime(Resource resource) {
        return simpleGraph(
                "maxSeries({ROOT}.resources." + resource.getMetricsPath() + ".max)",
                "maxSeries({ROOT}.resources." + resource.getMetricsPath() + ".p99)",
                "maxSeries({ROOT}.resources." + resource.getMetricsPath() + ".p95)")
                .withTitle(resource.getName() + " response time")
                .withSpan(6);
    }

    public SingleStat appInstances() {
        int okAbove = getEnvironment().getExpectedNumberOfInstances();
        int warningAbove = okAbove - 1;

        return counter("{ROOT}.noOfInstancesAlive", true)
                .withThresholds(warningAbove + "," + okAbove)
                .withPostfix("/" + getEnvironment().getExpectedNumberOfInstances())
                .withTitle("Instances");
    }

}
