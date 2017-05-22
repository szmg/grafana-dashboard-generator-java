package uk.co.szmg.grafana;

/*-
 * #%L
 * dashboard-builder-main
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

import org.junit.Ignore;
import org.junit.Test;
import uk.co.szmg.grafana.domain.Dashboard;
import uk.co.szmg.grafana.domain.Row;
import uk.co.szmg.grafana.domain.Target;

import java.io.IOException;

import static uk.co.szmg.grafana.domain.DomainFactories.newDashboard;
import static uk.co.szmg.grafana.domain.DomainFactories.newGraph;
import static uk.co.szmg.grafana.domain.DomainFactories.newRow;
import static uk.co.szmg.grafana.domain.DomainFactories.newSingleStat;
import static uk.co.szmg.grafana.domain.DomainFactories.newTarget;
import static uk.co.szmg.grafana.domain.DomainFactories.newText;

public class DashboardSerializerTest {

    private DashboardSerializer serializer = new DashboardSerializer();

    /**
     * Of course it's not really a test... yet.
     */
    @Test
    @Ignore
    public void printTestDashboard() throws IOException {
        //System.out.println(serializer.toString(testDashboard()));
        serializer.write(testDashboard(), System.out);
    }

    @Test
    @Ignore
    public void upload() {
        GrafanaEndpoint endpoint = new GrafanaEndpoint();
        endpoint.setBaseUrl(System.getProperty("grafana.url"));
        endpoint.setApiKey(System.getProperty("grafana.apiKey"));
//        endpoint.setSessionCookie("");
        endpoint.setSkipSSLValidation(true);
        DashboardUploader uploader = new DashboardUploader(endpoint);
        uploader.upload(DashboardSerializerTest.testDashboard(), true);

    }

    public static Dashboard testDashboard() {
        Target target = newTarget()
                .withTarget("maxSeries(humidity.peti.test.sensors)");

        Row row1 = newRow()
                .withHeight("100px")
                .addPanel(newSingleStat()
                        .withTitle("Single stat test")
                        .addTarget(target)
                        .withSpan(2))
                .addPanel(newText()
                        .withContent("<div class=\"text-center dashboard-header\"><span>This is the test</span></div>")
                        .withMode("html")
                        .withTransparent(true)
                        .withSpan(8))
                .addPanel(newSingleStat()
                        .withTitle("Single stat test")
                        .addTarget(target)
                        .withSpan(2));

        Row row2 = newRow()
                .addPanel(newGraph()
                        .addTarget(target)
                        .withSpan(12));

        return newDashboard()
                .withTitle("Test dashboard")
                .addRow(row1)
                .addRow(row2);
    }

}
