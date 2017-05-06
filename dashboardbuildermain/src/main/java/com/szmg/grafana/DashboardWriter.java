package com.szmg.grafana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szmg.grafana.domain.Dashboard;
import com.szmg.grafana.domain.Graph;
import com.szmg.grafana.domain.Row;
import com.szmg.grafana.domain.SingleStat;
import com.szmg.grafana.domain.Target;
import com.szmg.grafana.domain.Text;

import java.io.IOException;
import java.util.ArrayList;

public class DashboardWriter {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello! :)");

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        System.out.println(mapper.writeValueAsString(testDashboard()));
    }

    private static Dashboard testDashboard() {
        Dashboard dashboard = new Dashboard();
        dashboard.setTitle("Test dashboard");
        dashboard.setRows(new ArrayList<>());

        Row row1 = newRow(dashboard);
        Row row2 = newRow(dashboard);

        Text text = new Text().withContent("# This is the test").withSpan(8);
        row1.getPanels().add(text);

        SingleStat singleStat = new SingleStat();
        singleStat.setTitle("Test single stat");
        singleStat.setTargets(new ArrayList<>());
        singleStat.setSpan(4);
        row1.getPanels().add(singleStat);

        Graph graph = new Graph();
        graph.setSpan(12);
        graph.setTargets(new ArrayList<>());
        row2.getPanels().add(graph);

        Target target = new Target();
        target.setTarget("maxSeries(humidity.peti.test.sensors)");
        singleStat.getTargets().add(target);
        graph.getTargets().add(target);

        return dashboard;
    }

    private static Row newRow(Dashboard dashboard) {
        Row row = new Row();
        row.setHeight("200px");
        row.setPanels(new ArrayList<>());
        dashboard.getRows().add(row);
        return row;
    }

}
