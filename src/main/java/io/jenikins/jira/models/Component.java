package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Component {
    private String name;

    public String getName() {
        return name;
    }
}
