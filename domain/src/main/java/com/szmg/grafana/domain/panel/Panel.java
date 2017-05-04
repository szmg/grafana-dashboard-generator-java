package com.szmg.grafana.domain.panel;

/**
 * Based on
 * https://github.com/grafana/grafana/blob/master/public/app/partials/panelgeneral.html
 */
public abstract class Panel {

    private Integer id;
    private String title;
    private String description;
    private int span = 4;
    private String height;
    private boolean transparent = false;
    private Integer minSpan;
    //links
    //scopedVars

    public abstract String getType();

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSpan() {
        return span;
    }

    public void setSpan(int span) {
        this.span = span;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public boolean isTransparent() {
        return transparent;
    }

    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    public Integer getMinSpan() {
        return minSpan;
    }

    public void setMinSpan(Integer minSpan) {
        this.minSpan = minSpan;
    }
}
