Jira Issues Fetcher
===================

Jira Issues Fetcher is a maven project that runs on jenikins to pull the issues as per the jql QUERY configured in $QUERY parameter and sends the mail to respective assignees.

# Steps to create a jeniknis job

- Create a maven project on jenikins and configure the below details in it.
- Select the "This project is parameterized" check box under General tab and add a parameter name as `QUERY` , this will be used for jql Query.
- Repeat the above step for one more parameter named as `JIRA_HOST_URL`, this is the base url of hosted JIRA.
<img width="1391" alt="params" src="https://user-images.githubusercontent.com/13100987/123043119-7955cd00-d415-11eb-8e2b-bb2a86476733.png">

- Move to Source Code Management configuration and configure git as given in the snapshot below. Check your git credentials before configuring them.
<img width="1225" alt="git" src="https://user-images.githubusercontent.com/13100987/123043419-e4070880-d415-11eb-9e7f-817d3645b9f3.png">

- Check "Use secret text(s) or file(s)" under Build Management and Configure Bindings for `JIRA_USERNAME` & `JIRA_PASSWORD` and supply credentials for your Jira system for authentication.
<img width="1196" alt="jira" src="https://user-images.githubusercontent.com/13100987/123043948-a8207300-d416-11eb-8011-11a278c3c2e6.png">

- Add a Build step as given in snapshot below.
<img width="1172" alt="build" src="https://user-images.githubusercontent.com/13100987/123044275-136a4500-d417-11eb-9ab9-68da508d623c.png">

- In the end add a post build action to archive the artifacts (issues file & emails file created) and editable email notification to send emails , taking benefits of issues file & emails file created as artifacts post build.
* Note : Make use of editable email notification template to configure "from address" , "subject" and add bcc and cc if you wish to.
<img width="1424" alt="post_build" src="https://user-images.githubusercontent.com/13100987/123044881-e8342580-d417-11eb-9113-df774f01aff4.png">


# Paramters to be passed and steps to run

Jql query (QUERY) & JIRA_HOST_URL and are required to be passed as parameters while running the job.

Withing QUERY we can give header for the query to be sent as part of mail, the query and header should be separated with (>>).
Example : labels in (no-automated-test) AND component = Sustenance AND project = NPR >> Open Issues for 6.5.10

How it works
The script runs and reads parameters from $QUERY parameter and perform below steps.

- Login to jira using session API.
- Get query and header by splitting with (>>) if (>>) is added.
- Get issues for each query configured.
- Prepare the table and get list of assignees.
- Delete if applicable and newly create issues.html and emails.html and fill the respective enteries.
- Send the email using job configuration.
- Once the execution gets over, the archived file are deleted.
