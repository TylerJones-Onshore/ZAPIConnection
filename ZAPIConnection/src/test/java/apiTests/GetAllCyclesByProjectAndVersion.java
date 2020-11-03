package apiTests;

import org.testng.annotations.Test;

import zapi.ZapiClient;
import zapiObjects.ProjectCycles;
import zapiObjects.CycleList;

public class GetAllCyclesByProjectAndVersion {
	static ZapiClient c = new ZapiClient();
	@Test
	public static void GetAllCycles() {
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
		CycleList list = c.getCycleList(projectId, versionId);
		System.out.println("*****************\n\n\n");
		for(ProjectCycles cycle: list.cycles) {
			System.out.println(cycle.id);
			System.out.println(cycle.name);
		}
		
	}
}
