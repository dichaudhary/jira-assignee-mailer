package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Session {
    private String value;
    private String name;

    public String getName() {
        return name;
    }
    public String getValue() {
        return value;
    }

}
