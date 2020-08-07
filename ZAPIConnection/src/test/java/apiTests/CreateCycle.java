package apiTests;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.testng.annotations.Test;

import zapi.ZapiClient;
import zapiObjects.ProjectCycles;

public class CreateCycle {
	ZapiClient Zapi = new ZapiClient();
	
	@Test
	public void createCycle() {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";

		Zapi.setAccessKey(accessKey);
		Zapi.setAccountId("5bfc155710c30e4ac8c8a502");
		Zapi.setSecretKey(secretKey);
		
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    String date = formatter.format(new Date());
		
		ProjectCycles pc = Zapi.createCycle(date, "14937", "16174");
		System.out.println(pc.id);
		System.out.println(pc.name);
		
		
	}
}
