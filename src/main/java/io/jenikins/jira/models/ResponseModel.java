package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseModel {
    String total;
    List<Issue> issues;

    public String getTotal() {
        return total;
    }

    public List<Issue> getIssues() {
        return issues;
    }
}

