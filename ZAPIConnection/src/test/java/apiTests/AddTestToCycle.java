package apiTests;

import org.testng.annotations.Test;

import zapi.ZapiClient;

public class AddTestToCycle {
	ZapiClient Zapi = new ZapiClient();
	
	
	
	@Test
	public void addTestToCycle() {
		String accessKey = "accessKey";
		String secretKey = "secretKey";
		String accId = "jiraAccountId";
		String projectId="16174";
		String versionId="14937";
		String cycleId="dd48e274-f919-422d-b942-70d08de746f8";
		
		Zapi.setAccessKey(accessKey);
		Zapi.setSecretKey(secretKey);
		Zapi.setAccountId(accId);
		
		String[] tests = new String[] {"TES-23","TES-24","TES-25"};
		
		try {
			Zapi.addTestToCycle(cycleId, projectId, versionId, tests);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
