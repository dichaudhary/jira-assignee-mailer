package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Fields {

    private Issuetype issuetype;
    private Status status;
    private Assignee assignee;
    private List<Component> components;
    private String summary;

    public Issuetype getIssuetype() {
        return issuetype;
    }

    public Status getStatus() {
        return status;
    }

    public Assignee getAssignee() {
        return assignee;
    }

    public List<Component> getComponents() {
        return components;
    }

    public String getSummary() {
        return summary;
    }
}
