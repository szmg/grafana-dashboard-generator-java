package com.szmg.jsonbuildergenerator;

public class FieldDescription {

    private String name;
    private String type;
    private String description;
    private boolean readonly;

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReadonly() {
        return readonly;
    }
}
