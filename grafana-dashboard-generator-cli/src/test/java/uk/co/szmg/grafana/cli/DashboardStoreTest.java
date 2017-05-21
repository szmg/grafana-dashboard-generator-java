package uk.co.szmg.grafana.cli;

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

import org.junit.Test;
import uk.co.szmg.grafana.cli.internal.DashboardStore;
import uk.co.szmg.grafana.domain.Dashboard;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class DashboardStoreTest {

    @Test
    public void shouldFindDashboards() {
        DashboardStore store = new DashboardStore("uk.co.szmg.grafana.cli.example");
        store.load();
        List<Dashboard> dashboards = store.getDashboards();

        assertThat(dashboards.size(), is(1));
        assertThat(dashboards.get(0).getTitle(), is("Overview dashboard"));
    }

}
