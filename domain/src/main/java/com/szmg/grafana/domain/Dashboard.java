package com.szmg.grafana.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on http://docs.grafana.org/reference/dashboard/
 */
public class Dashboard {
    private Integer id;
    private String title = "New dashboard";
    private List<String> tags = new ArrayList<>();
    private String style = "dark";
    private String timezone = "browser";
    private boolean editable = true;
    private boolean hideControls = false;
    private Integer graphTooltip = 1;
    private List<Row> rows = new ArrayList<>();
    private FromTo time;
    // ...

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isEditable() {
        return editable;
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
    }

    public boolean isHideControls() {
        return hideControls;
    }

    public void setHideControls(boolean hideControls) {
        this.hideControls = hideControls;
    }

    public Integer getGraphTooltip() {
        return graphTooltip;
    }

    public void setGraphTooltip(Integer graphTooltip) {
        this.graphTooltip = graphTooltip;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public FromTo getTime() {
        return time;
    }

    public void setTime(FromTo time) {
        this.time = time;
    }
}
