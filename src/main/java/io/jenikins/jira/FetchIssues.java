package io.jenikins.jira;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.ObjectMapper;

import io.jenikins.jira.models.LoginResponse;
import io.jenikins.jira.models.ResponseModel;

import static io.jenikins.jira.Constants.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.stream.Collectors;

public class FetchIssues {
    private static final String JQL_KEY = "?jql=";
    private ObjectMapper objectMapper = new ObjectMapper();

    public void execute()   {
        String loginResponse = "";
        String jSessionID = "";
        String issuesData = "";
        
        String query = System.getProperty(QUERY);
        String loginUserName = System.getProperty(JIRA_USERNAME_PARAM);
        String loginPassWord = System.getProperty(JIRA_PASSWORD_PARAM);
        boolean errorsOccurred = false;

        if(!errorsOccurred)
        {
            loginResponse = loginToJira(loginUserName, loginPassWord);
            if(loginResponse == ERROR) { errorsOccurred = true; }
        }
        if(!errorsOccurred)
        {
            jSessionID = parseJSessionID(loginResponse);
            if(jSessionID == ERROR) { errorsOccurred = true; }
        }
        if(!errorsOccurred)
        {
            issuesData = listIssuesWithQuery(query.split(">>")[0].trim(), jSessionID);
            if(issuesData != ERROR) {
                try {
                    ResponseModel response = objectMapper.readValue(issuesData, ResponseModel.class);

                    Set<String> toEmails = response.getIssues().stream().map(issue -> {
                        return issue.getFields().getAssignee().getEmailAddress();
                    }).collect(Collectors.toSet());

                    MailDetailsAccumulator.getInstance().populateMailDetailsInFiles(toEmails, response.getIssues(), query.split(">>").length > 1 ? query.split(">>")[1].trim() : "", query);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * This method takes the user's credentials and uses them to make a request
     * to log into a given Jira instance.  It returns the response generated from
     * that request.
     * @param loginUserName The username of a user who has permissions to view the
     * issues which we would like to export out of Jira.
     * @param loginPassWord The password associated with the user named in
     * loginUserName.
     *
     * @return loginResponse The response generated by Jira and returned when this
     * method submits its login request.
     */
    public String loginToJira(String loginUserName, String loginPassWord){
        String loginResponse = "";
        URL url = null;
        HttpURLConnection conn = null;
        String input = "";
        OutputStream os = null;
        try {
            url = new URL(JIRA_LOGIN_URL);
            conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            input = "{\"username\":\"" + loginUserName + "\", \"password\":\"" + loginPassWord + "\"}";
            os = conn.getOutputStream();
            os.write(input.getBytes("utf-8"), 0, input.getBytes("utf-8").length);
            os.flush();
            System.out.println("error code: " + conn.getResponseCode());
            if(conn.getResponseCode() == 200){
                try(BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println(response.toString());
                    loginResponse = response.toString();
                }
                conn.disconnect();
            }
        } catch (Exception ex) {
            System.out.println("Error in loginToJira: " + ex.getMessage());
            loginResponse = ERROR;
        }
        return loginResponse;
    }


    /**
     * This method takes the response from a Jira login request and parses out
     * the JSESSIONID which will be saved and used to authenticate future
     * requests.
     *
     * @param input The response (in JSON format) from a Jira login request.
     *
     * @return jSessionID The value of the JSESSIONID assigned to this session
     * by the Jira server.
     */
    public String parseJSessionID(String input){
        try {
            LoginResponse login = objectMapper.readValue(input, LoginResponse.class);
            return login.getSession().getValue();
        } catch (Exception ex) {
            System.out.println("Error in parseJSessionID: " + ex.getMessage());
        }
        return ERROR;
    }

    public String listIssuesWithQuery(String query, String jSessionID){
        try {
            System.out.println("running query : " + query);
            String cookie = "JSESSIONID=" + jSessionID + ";";
            HttpGet get = new HttpGet(JIRA_SEARCH_URL + JQL_KEY + URLEncoder.encode(query, String.valueOf(StandardCharsets.UTF_8)));
            get.setHeader("Content-Type", "application/json");
            get.setHeader("Cookie", cookie);
            try (CloseableHttpClient client = HttpClients.createDefault();
                CloseableHttpResponse res = client.execute(get);) {
                System.out.println("error code for issues " + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(res.getEntity());
                }
            } catch (Exception e) {
                System.out.println("error in fetching issues from jira" + e.getMessage());
                return ERROR;
            }
        } catch (Exception ex){
            System.out.println("Error in getJsonData: " + ex.getMessage());
            return ERROR;
        }
        return ERROR;
    }

}
