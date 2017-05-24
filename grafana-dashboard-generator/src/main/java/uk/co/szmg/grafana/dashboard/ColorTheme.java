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

import uk.co.szmg.grafana.domain.Colors;

/**
 * Most dashboards have a common color palette. This class
 * is to store one of that. Extend it in case you'd need
 * more colors in your factories.
 *
 * There are 4 types of color:
 * <dl>
 *     <dt>neutral</dt>
 *     <dd>color for texts that are neither good nor bad; e.g., white</dd>
 *     <dt>healthy</dt>
 *     <dd>color to mark healthy things, e.g., green</dd>
 *     <dt>warning</dt>
 *     <dd>color used for the middle part of the thresholds, e.g., yellow</dd>
 *     <dt>error</dt>
 *     <dd>color that marks an error, e.g., red</dd>
 * </dl>
 *
 * Colors should be defined as html/css color strings.
 *
 * @see BasicFactories
 * @since 1.1
 */
public class ColorTheme {

    /**
     * A simple, red-yellow-green and white theme.
     */
    public static final ColorTheme RED_YELLOW_GREEN = new ColorTheme(
            "white",
            "rgba(50, 172, 45, 0.97)",
            "rgba(237, 129, 40, 0.89)",
            "rgba(245, 54, 54, 0.9)");

    private String neutral;
    private String healthy;
    private String warning;
    private String error;

    /**
     * Constructor.
     *
     * @param neutral neutral color for texts that are neither good nor bad; e.g., white
     * @param healthy color to mark healthy things, e.g., green
     * @param warning color used for the middle part of the thresholds, e.g., yellow
     * @param error color that marks an error, e.g., red
     */
    public ColorTheme(String neutral, String healthy, String warning, String error) {
        this.neutral = neutral;
        this.healthy = healthy;
        this.warning = warning;
        this.error = error;
    }

    /**
     * Gets neutral color.
     * @return neutral color
     */
    public String getNeutral() {
        return neutral;
    }

    /**
     * Gets healthy color.
     * @return healthy color
     */
    public String getHealthy() {
        return healthy;
    }

    /**
     * Gets warning color.
     * @return warning color
     */
    public String getWarning() {
        return warning;
    }

    /**
     * Gets error color.
     * @return error color
     */
    public String getError() {
        return error;
    }

    /**
     * Gets a color configuration used in singlestat for when low value is good,
     * e.g., number of errors.
     * @return color configuration for singlestat
     */
    public Colors colorsWhenLowIsHealthy() {
        return new Colors(healthy, warning, error);
    }

    /**
     * Gets a color configuration used in singlestat for when high value is good,
     * e.g., number of instances alive.
     * @return color configuration for singlestat
     */
    public Colors colorsWhenHighIsHealthy() {
        return new Colors(error, warning, healthy);
    }
}
