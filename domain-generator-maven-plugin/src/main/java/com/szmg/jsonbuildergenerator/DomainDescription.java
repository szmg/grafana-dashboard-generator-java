package com.szmg.jsonbuildergenerator;

import java.util.List;

public class DomainDescription {

    private String name;
    private List<FieldDescription> fields;

    public String getName() {
        return name;
    }

    public List<FieldDescription> getFields() {
        return fields;
    }
}
