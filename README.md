Jira Assignee Mailer
====================

Jira Assignee Mailer is a maven project that runs on jenikins to pull the issues as per the jql QUERY configured in $QUERY parameter and sends the mail to respective assignees.

# Steps to create a jeniknis job

- Create a maven project on jenikins and configure the below details in it.
- Select the "This project is parameterized" check box under General tab and add a parameter name as `QUERY` , this will be used for jql Query.
- Repeat the above step for one more parameter named as `JIRA_HOST_URL`, this is the base url of hosted JIRA.
<img width="1391" alt="params" src="https://user-images.githubusercontent.com/13100987/123043119-7955cd00-d415-11eb-8e2b-bb2a86476733.png">

- Move to Source Code Management configuration and configure git as given in the snapshot below. Check your git credentials before configuring them.
<img width="1244" alt="git" src="https://user-images.githubusercontent.com/13100987/123055395-7530ac00-d423-11eb-9ecd-709559b012a0.png">


- Check "Use secret text(s) or file(s)" under Build Management and Configure Bindings for `JIRA_USERNAME` & `JIRA_PASSWORD` and supply credentials for your Jira system for authentication.
<img width="1196" alt="jira" src="https://user-images.githubusercontent.com/13100987/123043948-a8207300-d416-11eb-8011-11a278c3c2e6.png">

- Add a Build step as given in snapshot below.
<img width="1399" alt="build" src="https://user-images.githubusercontent.com/13100987/123055536-a315f080-d423-11eb-87f6-c4277a6012f7.png">


- In the end add a post build action to archive the artifacts (issues file & emails file created) and editable email notification to send emails , taking benefits of issues file & emails file created as artifacts post build.
* Note : Make use of editable email notification template to configure "from address" , "subject" and add bcc and cc if you wish to.
<img width="1313" alt="mail" src="https://user-images.githubusercontent.com/13100987/123055767-da849d00-d423-11eb-9ebc-a85be1a9d4aa.png">


# Paramters to be passed and steps to run

Jql query (QUERY) & JIRA_HOST_URL and are required to be passed as parameters while running the job.

Withing QUERY we can give header for the query to be sent as part of mail, the query and header should be separated with (>>).
Example : labels in (no-automated-test) AND component = Sustenance AND project = NPR >> Open Issues for 6.5.10

# How it works

- The script runs and reads parameters from $QUERY parameter and perform below steps.
- Login to jira using session API.
- Get query and header by splitting with (>>) if (>>) is added.
- Get issues for each query configured.
- Prepare the table and get list of assignees.
- Delete if applicable and newly create issues.html and emails.html and fill the respective enteries.
- Send the email using job configuration.
- Once the execution gets over, the archived file are deleted.
