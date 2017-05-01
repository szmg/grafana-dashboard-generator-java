package com.szmg.grafana.domain;

public class FromTo {
    // TODO time type + utils
    private String from = "now-6h";
    private String to = "now";

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
