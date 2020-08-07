package apiTests;

import org.testng.annotations.Test;

import zapi.ZapiClient;
import zapiObjects.ProjectCycles;
import zapiObjects.CycleList;

public class GetAllCyclesByProjectAndVersion {
	static ZapiClient c = new ZapiClient();
	@Test
	public static void GetAllCycles() {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";
		String accId = "5bfc155710c30e4ac8c8a502";
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
		CycleList list = c.getCycleList(projectId, versionId);
		System.out.println("*****************\n\n\n");
		for(ProjectCycles cycle: list.cycles) {
			System.out.println(cycle.id);
			System.out.println(cycle.name);
		}
		
	}
}
