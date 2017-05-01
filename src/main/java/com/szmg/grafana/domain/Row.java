package com.szmg.grafana.domain;

import java.util.ArrayList;
import java.util.List;

public class Row {

    private boolean collapse = false;
    private boolean editable = true;
    private String height = "200px"; // TODO size type + utils
    private List<Panel> panels = new ArrayList<>();
    private String title = "New row";

    public boolean isCollapse() {
        return collapse;
    }

    public void setCollapse(boolean collapse) {
        this.collapse = collapse;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public List<Panel> getPanels() {
        return panels;
    }

    public void setPanels(List<Panel> panels) {
        this.panels = panels;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
