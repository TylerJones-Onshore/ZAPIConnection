package apiTests;

import org.testng.annotations.Test;

import zapi.Client;

public class GetAllCyclesByProjectAndVersion {
	static Client c = new Client();
	@Test
	public static void GetAllCycles() {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";
		String accId = "5bfc155710c30e4ac8c8a502";
		String projectId = "16174";
		String versionId = "14937";
		
		c.setAccessKey(accessKey);
		c.setSecretKey(secretKey);
		c.contentType = "test/plain";
		c.getCycleList(accId, projectId, versionId, 0);
		
		
		
	}
}
