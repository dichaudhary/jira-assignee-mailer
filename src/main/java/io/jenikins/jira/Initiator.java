package io.jenikins.jira;

public class Initiator {

    public static void main (String[] args) {
        System.out.println("starting the process");
        FetchIssues fetchIssues = new FetchIssues();
        fetchIssues.execute();
    }
}
