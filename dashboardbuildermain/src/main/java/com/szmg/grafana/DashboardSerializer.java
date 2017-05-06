package com.szmg.grafana;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.szmg.grafana.domain.Dashboard;
import com.szmg.grafana.domain.Graph;
import com.szmg.grafana.domain.Row;
import com.szmg.grafana.domain.Target;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import static com.szmg.grafana.domain.DomainFactories.newDashboard;
import static com.szmg.grafana.domain.DomainFactories.newRow;
import static com.szmg.grafana.domain.DomainFactories.newSingleStat;
import static com.szmg.grafana.domain.DomainFactories.newTarget;
import static com.szmg.grafana.domain.DomainFactories.newText;

public class DashboardSerializer {

    private final ObjectMapper mapper;

    public DashboardSerializer() {
        mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    public String toString(Dashboard dashboard) {
        try {
            return mapper.writeValueAsString(dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    public void write(Dashboard dashboard, Writer writer) throws IOException {
        try {
            mapper.writeValue(writer, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

    public void write(Dashboard dashboard, OutputStream stream) throws IOException {
        try {
            mapper.writeValue(stream, dashboard);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("dashboard cannot be serialized to JSON." +
                    "It might contain non-serializable values.", e);
        }
    }

}
