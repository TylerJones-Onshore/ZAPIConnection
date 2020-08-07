package apiTests;

import org.testng.annotations.Test;

import jiraObjects.Issue;
import zapi.ZapiClient;
import zapiObjects.ExecutionDetails;
import zapiObjects.ExecutionStatus;

public class UpdateExecution {
	ZapiClient c = new ZapiClient();
	@Test
	public void updateExecution() {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";
		String accId = "5bfc155710c30e4ac8c8a502";
		String projectId = "16174";
		String versionId = "14937";
		
		c.setAccountId(accId);
		c.setExperation(2000);
		c.setAccessKey(accessKey);
		c.setSecretKey(secretKey);

		c.contentType = "application/json";
		
		ExecutionDetails details = new ExecutionDetails();
		details.executionId = "d5a798ba-4d21-478f-9972-6cbe835e21d1";
		details.status = new ExecutionStatus();
		details.status.id = 2;
		details.projectId = Integer.parseInt(projectId);
		details.versionId =Integer.parseInt(versionId);
		details.issueId = 97136;
		details.defects = new Issue[2];
		Issue first = new Issue();
		first.id = "99369";
		Issue second = new Issue();
		second.id = "99372";
		details.defects[0] = first;
		details.defects[1] = second;
		
		if(c.updateExecution(details.executionId, details)) {
			System.out.println("Success!");
		}
		else {
			System.out.println("Failure!");
		}
		
		
	}
}
