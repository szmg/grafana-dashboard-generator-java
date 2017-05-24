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

import uk.co.szmg.grafana.domain.Row;
import uk.co.szmg.grafana.domain.Text;

import static uk.co.szmg.grafana.domain.DomainFactories.newRow;
import static uk.co.szmg.grafana.domain.DomainFactories.newText;

/**
 * Simple, stupid static generator functions.
 *
 * See {@link BasicFactories} for more complex generators.
 *
 * @see BasicFactories
 * @since 1.1
 */
public class StaticFactories {

    /**
     * Creates a row with height of 95px. It is usually good
     * for single number metrics (singlestat) and texts.
     *
     * @return new row that has a height of 95px
     */
    public static Row thinRow() {
        return newRow().withHeight("95px");
    }

    /**
     * Creates a nice title text panel, just like the one on the default home dashboard.
     * It does not set the width (span).
     *
     * @param title the title in HTML
     * @return new text panel
     */
    public static Text title(String title) {
        return newText()
                .withContent("<div class=\"text-center dashboard-header\"><span>" + title + "</span></div>")
                .withMode("html")
                .withTransparent(true);
    }

    /**
     * Creates a totally empty and transparent text panel with the given width.
     *
     * @param span width
     * @return new text panel
     */
    public static Text placeholder(int span) {
        return newText()
                .withContent("")
                .withTransparent(true)
                .withSpan(span);
    }

}
