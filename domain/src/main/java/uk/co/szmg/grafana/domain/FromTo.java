package uk.co.szmg.grafana.domain;

/*-
 * #%L
 * domain
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
 * Time range that is used in Grafana Dashboards.
 * By default it's from "now-6h" to "now". Look up Grafana docs for valid values.
 */
public class FromTo {
    // TODO time type + utils
    private String from;
    private String to;

    /**
     * Default constructor with default values: from "now-6h" to "now".
     */
    public FromTo() {
        this("now-6h", "now");
    }

    /**
     * Convenience constructor.
     * @param from start of the displayed time range
     * @param to end of the displayed time range
     */
    public FromTo(String from, String to) {
        this.from = from;
        this.to = to;
    }

    /**
     * Gets the start of the displayed time range.
     * @return Grafana time expression
     */
    public String getFrom() {
        return from;
    }

    /**
     * Sets the start of the displayed time range.
     * @param from Grafana time expression
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Gets the end of the displayed time range.
     * @return Grafana time expression
     */
    public String getTo() {
        return to;
    }

    /**
     * Sets the end of the displayed time range.
     * @param to Grafana time expression
     */
    public void setTo(String to) {
        this.to = to;
    }
}
