package com.szmg.jsonbuildergenerator;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class DomainDescription {

    private String name;

    private List<FieldDescription> fields;

    private Map<String, String> defaultValues;

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

    public Map<String, String> getDefaultValues() {
        return defaultValues;
    }

    public boolean isAbstract() {
        return isAbstract;
    }

    public String getExtendedClass() {
        return extendedClass;
    }
}
