package com.szmg.grafana;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szmg.grafana.domain.Text;

import java.io.IOException;

public class DashboardWriter {

    public static void main(String args[]) throws IOException {
        System.out.println("Hello! :)");

        Text t = new Text();
        t.setContent("nothing");
        t.setMode("markdown");

        ObjectMapper mapper = new ObjectMapper();

        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        System.out.println(mapper.writeValueAsString(t));

    }

}
