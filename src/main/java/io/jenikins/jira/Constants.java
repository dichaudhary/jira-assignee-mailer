package io.jenikins.jira;

/**
 * Global constants for jenkins Dashboard.
 * 
 */
public final class Constants {

	public static final String JIRA_HOST_URL_PARAM = "JIRA_HOST_URL";

	public static final String JIRA_REST_URL_VALUE = System.getProperty(JIRA_HOST_URL_PARAM) + "/rest";


	public static final String JIRA_BASE_URL =  JIRA_REST_URL_VALUE + "/browse/";

	public static final String JIRA_PROFILE_URL = JIRA_REST_URL_VALUE + "/secure/ViewProfile.jspa";

	public static final String JIRA_LOGIN_URL = JIRA_REST_URL_VALUE + "/auth/1/session";

	public static final String JIRA_SEARCH_URL = JIRA_REST_URL_VALUE + "/api/2/search";

	public static final String ERROR = "ERROR";

	public static final String JIRA_USERNAME_PARAM = "JIRA_USERNAME";

	public static final String JIRA_PASSWORD_PARAM = "JIRA_PASSWORD";

	public static final String QUERY = "QUERY";
}
