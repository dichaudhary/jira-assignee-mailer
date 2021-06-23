
package io.jenikins.jira;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import io.jenikins.jira.models.Component;
import io.jenikins.jira.models.Issue;
import static io.jenikins.jira.Constants.*;


public class MailDetailsAccumulator {
	private static volatile MailDetailsAccumulator instance;
	private static Object mutex = new Object();

    private MailDetailsAccumulator() {}

    public static MailDetailsAccumulator getInstance() {
		MailDetailsAccumulator result = instance;
		if (result == null) {
			synchronized (mutex) {
				result = instance;
				if (result == null)
					instance = result = new MailDetailsAccumulator();
			}
		}
		return result;
	}

	public void populateMailDetailsInFiles(Set<String> mailTo, List<Issue> issues, String header, String query) {
		try {
			File issuesFile = new File("issues.html");
			File toEmailsFile = new File("emails.html");
			if (issuesFile.createNewFile()) {
			  System.out.println("File created: " + issuesFile.getName());
			} else {
			  System.out.println("File already exists deleting...");
			  issuesFile.delete();
			  if (issuesFile.createNewFile()) {
				System.out.println("File created: " + issuesFile.getName());
			  } 
			}
			if (toEmailsFile.createNewFile()) {
				System.out.println("File created: " + toEmailsFile.getName());
			} else {
				System.out.println("File already exists deleting...");
				toEmailsFile.delete();
				if (toEmailsFile.createNewFile()) {
				  System.out.println("File created: " + toEmailsFile.getName());
				} 
			}
			if (issuesFile.exists() && toEmailsFile.exists()) {
				FileWriter issuesWriter = new FileWriter(issuesFile);
				FileWriter emailsWriter = new FileWriter(toEmailsFile);
				issuesWriter.write(createMailBody(issues, header, query));
				emailsWriter.write(mailTo.stream().collect(Collectors.joining(",")));
				issuesWriter.close();
				emailsWriter.close();
			}
			System.out.println("Successfully written content to the file.");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	/**
	 * Compose email
	 *
	 * @return email body
	 */
	private String createMailBody(List<Issue> issues, String header, String query) {

		String body = "<html><style>table{border:1px solid black;border-collapse:collapse}tr:nth-child(2n+1) { background: #eee; }th {   background: #555555;   color: white;   font-weight: bold; }td, th { padding: 5px;  border: 1px solid #ccc; text-align: left; }</style><body>";
		//body = body + "<center><span style='font-size:18px;'><b>" + this.dashboardName + "</b></span></center>";

		body = body + "</br><span style='font-size:18px;'><b>" + header + "| Query [0].</b></span></br>";
		body = body + "<table style='width:95%'>";
		body = body + " <tr> ";
		body = body + "<th>Type</th>";
		body = body + "<th>Key</th>";
		body = body + "<th>Status</th>";
		body = body + "<th>Assignee</th>";
		body = body + "<th>Components</th>";
		body = body + "<th>Summary</th>";

		for(Issue issue: issues){

			try {
				body = body + " <tr> ";
				body = body + "<td>";
				body = body + issue.getFields().getIssuetype().getName();
				body = body + " </td><td>";
				body = body + " <a href='" + JIRA_BASE_URL + issue.getKey() + "'>" + issue.getKey() + " </a>";
				body = body + "\t</td><td>";
				body = body + issue.getFields().getStatus().getName();
				body = body + "\t</td><td>";
				body = body + " <a href='" + JIRA_PROFILE_URL + "?name=" + issue.getFields().getAssignee().getName() + "'>" + issue.getFields().getAssignee().getDisplayName() + " </a>";
				body = body + "\t</td><td>";
				for (Component component : issue.getFields().getComponents()) {
					String href = JIRA_BASE_URL + issue.getKey() + "?jql=project = CQ AND component = " + component.getName();
					body = body + "<a href='" + href + "'>" + component.getName() + "</a> ";
				}
				body = body + "\t</td><td>";
				body = body + " <a href='" + JIRA_BASE_URL + issue.getKey() + "'>" + issue.getFields().getSummary() + " </a>";
				body = body + "</td>";
				body = body + "</tr>\t";
			} catch (Exception e) {
				System.out.println("Exception in Test jobs");
				e.printStackTrace();
			}
		}

		body = body + "</table>";
		return body;
	}

}
