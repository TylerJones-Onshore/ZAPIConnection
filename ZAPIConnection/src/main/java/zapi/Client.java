package zapi;

import static io.restassured.RestAssured.given;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.helpers.MessageFormatter;

import io.jsonwebtoken.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Client {
	private final String baseUri = "https://prod-api.zephyr4jiracloud.com";
	private final String contextPath = "/connect";
	private String zAccessKey;
	private String zSecretKey;
	public String contentType="";
	public Map<String,String> headers = new HashMap<String,String>();
	Map<String,Object> claims = new HashMap<String,Object>();
	public void setAccessKey(String key) {
		zAccessKey = key;
	}
	public void setSecretKey(String key) {
		zSecretKey = key;
	}
	
	public String getCycleList(String accId,String projectId, String versionId,long experation) {
		if(experation ==0) {
			experation =2000;
		}
		String relativePath = "/public/rest/api/1.0/cycles/search";
		String querryString = MessageFormatter.format("projectId={}&versionId={}",projectId,versionId).getMessage();
		String cannonicalPath = "GET&"+relativePath+"&"+querryString; 
		
		claims.clear();
		claims.put("sub", accId);
		claims.put("qsh", getQsh(cannonicalPath));
		claims.put("iss", zAccessKey);
		claims.put("iat", System.currentTimeMillis());
		claims.put("exp", System.currentTimeMillis()+experation);
		
		String jwt = getJwt(claims,zSecretKey);
		
		headers.clear();
		headers.put("Authorization", "JWT "+jwt);
		headers.put("zapiAccessKey",zAccessKey);
		
		Response r = get(contextPath+relativePath+"?"+querryString);
		
		
		return r.then().log().all().extract().asString();
	}
	
	private Response get(String resource) {
		RequestSpecification request = given().baseUri(baseUri);
		if (!contentType.equals("")) {
			request = request.contentType(contentType);
		}
		if (headers.size() > 0) {
			request = request.headers(headers);
		}
		return request.log().all().when().get(resource).andReturn();
	}
	
	public String getQsh(String qstring) {
		String qsh = "";
		try {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] bytes = digest.digest(qstring.getBytes(StandardCharsets.UTF_8));

		qsh = bytesToHex(bytes);
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		return qsh;
	}

	private String bytesToHex(byte[] bytes) {
		StringBuffer buffer = new StringBuffer();
		for (byte b : bytes) {
			String hex = String.format("%02x", b);
			if (hex.length() == 1) {
				buffer.append('0');
			}
			buffer.append(hex);
		}
		return buffer.toString();
	}

	public String getJwt(Map<String,Object> claims, String secret) {
		try {
			
		SignatureAlgorithm signature = SignatureAlgorithm.HS256;
		
		//byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
		byte[] apiKeySecretBytes = secret.getBytes("UTF-8");
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signature.getJcaName());
		JwtBuilder builder = Jwts.builder();
		builder.setHeaderParam("typ", "JWT");
		builder.setClaims(claims);
		//builder.signWith(signingKey, signature);
		builder.signWith(signingKey);
		//builder.signWith(signature,signingKey);
		
		return builder.compact();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			return e.getMessage();
		}
	}
}