package com.szmg.jsonbuildergenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class DomainDescription {

    private String name;

    private List<FieldDescription> fields;

    @JsonProperty("abstract")
    private boolean isAbstract;

    @JsonProperty("extends")
    private String extendedClass;

    public String getName() {
        return name;
    }

    public List<FieldDescription> getFields() {
        return fields;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getExtendedClass() {
        return extendedClass;
    }
}
