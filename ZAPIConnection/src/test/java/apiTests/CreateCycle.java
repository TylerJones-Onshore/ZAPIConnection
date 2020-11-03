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
		String accessKey = "accessKey";
		String secretKey = "secretKey";
		String accId = "jiraAccountId";

		Zapi.setAccessKey(accessKey);
		Zapi.setAccountId(accId);
		Zapi.setSecretKey(secretKey);
		
	    SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	    String date = formatter.format(new Date());
		
		ProjectCycles pc = Zapi.createCycle(date, "14937", "16174");
		System.out.println(pc.id);
		System.out.println(pc.name);
		
		
	}
}
