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

import uk.co.szmg.grafana.domain.Graph;
import uk.co.szmg.grafana.domain.SingleStat;
import uk.co.szmg.grafana.domain.Target;

import static uk.co.szmg.grafana.domain.DomainFactories.newGraph;
import static uk.co.szmg.grafana.domain.DomainFactories.newSingleStat;
import static uk.co.szmg.grafana.domain.DomainFactories.newTarget;

/**
 * An extensible dashboard component factory.
 *
 * <p>This provides basic builders for singlestat and graphs that set the
 * datasource and changes {@code {ROOT}} in the targets to {@link Environment#getMetricsRoot()}.

 * <p>It is intended to be extended, although it can be used on its own as well.
 * When extended, putting most of your business related generators in the child class(es)
 * is recommended, so the dashboard configuration can look much cleaner.
 *
 * <p>The main diferrence from the {@link StaticFactories} class is that here we have a context. Actually,
 * two context objects: a {@link ColorTheme} and an {@link Environment} implementation.
 * The latter is likely to be overridden, hence the type parameter.
 *
 * <p>See an example of this in the examples project.
 *
 * @since 1.1
 * @see Environment
 * @see SimpleEnvironment
 * @see ColorTheme
 * @param <ENV> the environment type, e.g., {@link SimpleEnvironment}
 */
public class BasicFactories<ENV extends Environment> {

    private ENV environment;
    private ColorTheme colorTheme;

    /**
     * Constructor.
     * @param environment environment that this factory should work for
     * @param colorTheme color theme
     */
    public BasicFactories(ENV environment, ColorTheme colorTheme) {
        this.environment = environment;
        this.colorTheme = colorTheme;
    }

    /**
     * A singlestat panel with span of 2.
     *
     * <p>Datasource and {@code {ROOT}} are set/evaluated with the environment object.
     * @param target the Graphite(?) query
     * @return new singlestat
     */
    public SingleStat neutralCounter(String target) {
        return newSingleStat()
                .withSpan(2)
                .addTarget(resolveTarget(target))
                .withDatasource(getEnvironment().getDatasource());
    }

    /**
     * A {@link #neutralCounter(String)} that is colored. Higher values are better.
     * Thresholds are still need to be set!
     *
     * <p>Datasource and {@code {ROOT}} are set/evaluated with the environment object.
     *
     * @param target target query
     * @param important if {@code true}, the background is colored, otherwise the text
     * @return new singlestat
     */
    public SingleStat counter(String target, boolean important) {
        return neutralCounter(target)
                .withColors(getColorTheme().colorsWhenHighIsHealthy())
                .withColorBackground(important)
                .withColorValue(!important);
    }

    /**
     * A {@link #neutralCounter(String)} that is colored. Lower values are better.
     * Thresholds are still need to be set!
     *
     * <p>Datasource and {@code {ROOT}} are set/evaluated with the environment object.
     *
     * @param target target query
     * @param important if {@code true}, the background is colored, otherwise the text
     * @return new singlestat
     */
    public SingleStat errorCounter(String target, boolean important) {
        return counter(target, important)
                .withColors(getColorTheme().colorsWhenLowIsHealthy());
    }

    /**
     * Creates a new graph with the given targets. The targets are added in
     * order and are referenced with uppercase letters ('A', 'B', ...)
     * Expect surprise when adding more than 26 targets.
     *
     * <p>Datasource and {@code {ROOT}} are set/evaluated with the environment object.
     *
     * @param targets targets
     * @return new graph with targets
     */
    public Graph simpleGraph(String... targets) {
        Graph graph = newGraph().withDatasource(getEnvironment().getDatasource());
        char ref = 'A';
        for (String target : targets) {
            graph.addTarget(resolveTarget(target).withRefId(String.valueOf(ref)));
            ref++;
        }
        return graph;
    }

    /**
     * Creates a Target object from the given query string. Also replaces
     * {@code {ROOT}} with {@link Environment#getMetricsRoot()}.
     *
     * @param target
     * @return
     */
    protected Target resolveTarget(String target) {
        return newTarget()
                .withTarget(target.replaceAll("\\{ROOT}", getEnvironment().getMetricsRoot()));
    }

    /**
     * Gets the current environment.
     * @return environment
     */
    public ENV getEnvironment() {
        return environment;
    }

    /**
     * Gets the current color theme.
     * @return color theme
     */
    public ColorTheme getColorTheme() {
        return colorTheme;
    }

}
