package com.szmg.grafana.domain.panel;

import com.szmg.grafana.domain.Panel;

/**
 * https://github.com/grafana/grafana/tree/master/public/app/plugins/panel/singlestat
 */
public class Singlestat extends Panel {

    private boolean colorBackground;
    private boolean colorValue;
    private Colors colors;
    private String datasource;
    private String format = "none"; // or short, etc.
    private Gauge gauge;



    @Override
    public String getType() {
        return "singlestat";
    }
}
