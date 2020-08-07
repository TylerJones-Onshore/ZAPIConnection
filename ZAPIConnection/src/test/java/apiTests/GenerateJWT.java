package apiTests;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import zapi.ZapiClient;

public class GenerateJWT {
	ZapiClient zCon = new ZapiClient();
	Map<String,Object> claims = new HashMap<String,Object>();
	
	@Test
	public void TestingQSHandJWT() throws NoSuchAlgorithmException, UnsupportedEncodingException {
		String accessKey = "ZjJlMThkNjktNzU5ZC0zNTgzLTkwYWMtNGY2Mzc5YzFlMjIyIDViZmMxNTU3MTBjMzBlNGFjOGM4YTUwMiBVU0VSX0RFRkFVTFRfTkFNRQ";
		String secretKey = "v7NJjgHp5FSRzNtVmswcXGyIxXjUmefFydKPTqkHVqQ";
		long experation = 35000;
		
		
		/*
		 */
		claims.put("sub", "5bfc155710c30e4ac8c8a502");
		claims.put("qsh", zCon.getQsh("POST&/public/rest/api/1.0/executions/add/cycle/44534520-177e-431a-9e2a-c1d50034293b&"));
		claims.put("iss", accessKey);
		claims.put("iat", System.currentTimeMillis());
		claims.put("exp", System.currentTimeMillis()+experation);
		String jwt = zCon.getJwt(claims, secretKey);
		//String jwt = zCon.createJWT(accessKey, "5bfc155710c30e4ac8c8a502", 3500, secretKey);
		System.out.println(claims.get("qsh"));
		System.out.println(jwt);
	}
}