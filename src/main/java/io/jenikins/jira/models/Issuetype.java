package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issuetype {

    private String id;
    private String name;

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }

}
