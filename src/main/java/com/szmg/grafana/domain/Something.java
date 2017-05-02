package com.szmg.grafana.domain;

public class Something extends BaseJsonObject {

    private static final String FIELD_ID = "id";
    private static final String FIELD_NAME = "name";

    public void setId(int id) {
        addValue(FIELD_ID, id);
    }

    public Integer getId() {
        return getValue(FIELD_ID);
    }

    public void setName(String name) {
        addValue(FIELD_NAME, name);
    }

    public String getName() {
        return getValue(FIELD_NAME);
    }
}
