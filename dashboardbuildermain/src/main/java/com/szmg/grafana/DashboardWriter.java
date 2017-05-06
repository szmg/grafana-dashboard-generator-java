package com.szmg.grafana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szmg.grafana.domain.Dashboard;
import com.szmg.grafana.domain.Graph;
import com.szmg.grafana.domain.Row;
import com.szmg.grafana.domain.Target;

import java.io.IOException;

import static com.szmg.grafana.domain.DomainFactories.newDashboard;
import static com.szmg.grafana.domain.DomainFactories.newRow;
import static com.szmg.grafana.domain.DomainFactories.newSingleStat;
import static com.szmg.grafana.domain.DomainFactories.newTarget;
import static com.szmg.grafana.domain.DomainFactories.newText;

public class DashboardWriter {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello! :)");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(mapper.writeValueAsString(testDashboard()));
    }

    private static Dashboard testDashboard() {
        Target target = newTarget().withTarget("maxSeries(humidity.peti.test.sensors)");

        Row row1 = newTallRow()
                .addPanel(newText().withContent("# This is the test").withSpan(8))
                .addPanel(newSingleStat().withTitle("Single stat test").addTarget(target).withSpan(4));

        Row row2 = newTallRow()
                .addPanel(new Graph().addTarget(target).withSpan(12));

        return newDashboard()
                .withTitle("Test dashboard")
                .addRow(row1)
                .addRow(row2);
    }

    private static Row newTallRow() {
        return newRow().withHeight("200px");
    }

}
