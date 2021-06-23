package io.jenikins.jira.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginResponse {
    private Session session;

    public Session getSession() {
        return session;
    }

}

