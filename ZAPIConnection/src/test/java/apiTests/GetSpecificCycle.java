package apiTests;

import org.testng.annotations.Test;

import zapi.ZapiClient;
import zapiObjects.CycleDetails;
import zapiObjects.Execution;

public class GetSpecificCycle {
	static ZapiClient c = new ZapiClient();
	@Test
	public static void GetCycle() {
		String accessKey = "accessKey";
		String secretKey = "secretKey";
		String accId = "jiraAccountId";
		String projectId = "16174";
		String versionId = "14937";
		/*
		String projectId = "14800";
		String versionId= "14507";
		 */
		c.setAccountId(accId);
		c.setExperation(2000);
		c.setAccessKey(accessKey);
		c.setSecretKey(secretKey);
		c.contentType = "test/plain";
		CycleDetails cycle = c.getCycle(projectId, versionId, "7e292fb7-8d6d-438f-944e-f729583244fe");
		System.out.println("*****************\n\n\n");
		System.out.println(cycle.totalCount);
		System.out.println(cycle.searchObjectList.length);
		
		//System.out.print("Searching for test KCA-104 in the cycle execution list...");
		for(Execution e: cycle.searchObjectList) {
			System.out.println(e.issueKey+"\n");
			System.out.println(e.execution.id+"\n");
			/*if(e.issueKey.equals("KCA-104")) {
				System.out.println("Test Found!");
				System.out.println("Validating test data...");
				System.out.print("Status : ");
				if(e.execution.status.name.equals("FAIL")) {
					System.out.println("CORRECT!");
				}
				else {
					System.out.println("FAILURE! INCORRECT DATA!");
				}
				break;
			}*/
		}
	}
}
