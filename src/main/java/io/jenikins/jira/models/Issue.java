package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Issue {

    private String key;
    private Fields fields;

    public String getKey() {
        return key;
    }
    public Fields getFields() {
        return fields;
    }

}
