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

public enum Resource {
    USER_REGISTRATION_REQUEST("user.registration", "User registration"),
    FORGOTTEN_PASSWORD("paswordhelp", "Forgatten password");

    Resource(String name, String metricsPath) {
        this.name = name;
        this.metricsPath = metricsPath;
    }

    private String name;
    private String metricsPath;

    public String getName() {
        return name;
    }

    public String getMetricsPath() {
        return metricsPath;
    }
}
