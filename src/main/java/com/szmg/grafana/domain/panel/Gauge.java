package com.szmg.grafana.domain.panel;

public class Gauge {

    private boolean show = true; // default is false, but if this class is used it might needed to be shown
    private int minValue = 0; // int? not float?
    private int maxValue = 100;
    private boolean thresholdMarkers = true;
    private boolean thresholdLabels = false;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public boolean isThresholdMarkers() {
        return thresholdMarkers;
    }

    public void setThresholdMarkers(boolean thresholdMarkers) {
        this.thresholdMarkers = thresholdMarkers;
    }

    public boolean isThresholdLabels() {
        return thresholdLabels;
    }

    public void setThresholdLabels(boolean thresholdLabels) {
        this.thresholdLabels = thresholdLabels;
    }
}
