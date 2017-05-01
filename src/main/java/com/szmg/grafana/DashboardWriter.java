package com.szmg.grafana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szmg.grafana.domain.Dashboard;
import com.szmg.grafana.domain.Row;
import com.szmg.grafana.domain.panel.Text;

import java.io.IOException;

public class DashboardWriter {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello! :)");

        Text p1 = new Text();

        Row row = new Row();
        row.getPanels().add(p1);

        Dashboard d = new Dashboard();
        d.getRows().add(row);

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(mapper.writeValueAsString(d));
    }

}
