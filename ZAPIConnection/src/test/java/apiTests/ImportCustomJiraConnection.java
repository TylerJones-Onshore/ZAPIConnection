package apiTests;

import jira.Client;
import jiraObjects.Issue;

public class ImportCustomJiraConnection {
	public static void main(String[] args) {
		Client c = new Client("tjones@aarp.org","0yw8ZD5F9Xn7p2gVAF8pC588");
		c.baseUri = "https://aarpqmo.atlassian.net";
		c.contentType = "application/json";
		Issue i = c.getIssue("TES-1");
		System.out.println(i.key);
	}
}