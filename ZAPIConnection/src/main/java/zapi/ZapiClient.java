package zapi;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.helpers.MessageFormatter;

import com.google.gson.Gson;

import io.jsonwebtoken.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import zapiObjects.ProjectCycles;
import zapiObjects.CycleDetails;
import zapiObjects.CycleList;
import zapiObjects.Execution;
import zapiObjects.ExecutionDetails;

public class ZapiClient {
	private final String baseUri = "https://prod-api.zephyr4jiracloud.com";
	private final String contextPath = "/connect";
	private String zAccessKey;
	private String zSecretKey;
	private long experation = 2000;
	private String accId = "";
	public String contentType="";
	public void setAccountId(String id) {
		accId = id;
	}
	public void setExperation(long time) {
		experation = time;
	}
	public Map<String,String> headers = new HashMap<String,String>();
	Map<String,Object> claims = new HashMap<String,Object>();
	Gson g = new Gson();
	public void setAccessKey(String key) {
		zAccessKey = key;
	}
	public void setSecretKey(String key) {
		zSecretKey = key;
	}
	public void addTestToCycle(String cycleId,String projectId,String versionId,String[] tests) throws Exception {
		String relativePath = "/public/rest/api/1.0/executions/add/cycle/"+cycleId;
		///public/rest/api/1.0/executions/add/cycle/44534520-177e-431a-9e2a-c1d50034293b
		String cannonicalPath = "POST&"+relativePath+"&";
		
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
		headers.put("Content-Type", "application/json");
		
		String json = "{\"versionId\":\""+versionId+"\",\"projectId\":\""+projectId+"\",\"method\":\"1\",\"issues\":[";
		for(int i = 0; i < tests.length; i ++) {
			if(i>0) {
				json+=",";
			}
			json+="\""+tests[i]+"\"";
		}
		json+="]}";
		
		Response r = post(baseUri+contextPath+relativePath,json);
		
		if(!(r.getStatusCode()>=200&&r.getStatusCode()<300)) {
			throw new Exception("Failed to add tests to cycle "+cycleId);
		}
		
		
	}
	
	public ProjectCycles createCycle(String name, String versionId, String projectId) {
		
		String relativePath = "/public/rest/api/1.0/cycle";
		String cannonicalPath = "POST&"+relativePath+"&";
		
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
		headers.put("Content-Type", "application/json");
		
		String json = "{\"name\":\""+name+"\",\"versionId\":\""+versionId+"\",\"projectId\":\""+projectId+"\"}";
		
		Response r = post(baseUri+contextPath+relativePath,json);
		
		return g.fromJson(r.then().log().all().extract().asString(),ProjectCycles.class);
	}
	
	public CycleDetails getCycle(String projectId, String versionId, String cycleId) {
		CycleDetails cycle = new CycleDetails();
		if(experation ==0) {
			experation =2000;
		}
		String relativePath = "/public/rest/api/1.0/executions/search/cycle/"+cycleId;
		String querryStringBase = "offset={}&projectId={}&versionId={}";
		
		int total =0;
		Object[] params= new Object[3];
		params[0] = 0;
		params[1] = projectId;
		params[2] = versionId;
		
		do {
		String querryString = MessageFormatter.arrayFormat(querryStringBase, params).getMessage();		
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
		//cycle = g.fromJson(r.then().log().all().extract().asString(),ProjectCycles[].class);
		if(cycle.searchObjectList==null) {
			cycle = g.fromJson(r.then().log().all().extract().asString(),CycleDetails.class);
			System.out.println("Total records expected: "+cycle.totalCount);
		}
		else {
			CycleDetails partial = new CycleDetails();
			partial =g.fromJson(r.then().log().all().extract().asString(),CycleDetails.class);
			CycleDetails temp = new CycleDetails();
			temp.searchObjectList = new Execution[cycle.searchObjectList.length+partial.searchObjectList.length];
			System.arraycopy(cycle.searchObjectList, 0, temp.searchObjectList, 0, cycle.searchObjectList.length);
			System.arraycopy(partial.searchObjectList, 0, temp.searchObjectList, cycle.searchObjectList.length, partial.searchObjectList.length);
			cycle.searchObjectList = temp.searchObjectList;
		}
		params[0] = (Integer)params[0]+cycle.maxAllowed;
		if(total<1) {
			total = cycle.totalCount;			
		}
		}while((Integer)params[0]<total);
		
		return cycle;
	}
	
	public CycleList getCycleList(String projectId, String versionId) {
		CycleList list = new CycleList();
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
		list.cycles = g.fromJson(r.then().log().all().extract().asString(),ProjectCycles[].class);
		return list;
	}
	
	public boolean updateExecution(String executionId, ExecutionDetails update) {
		String relativePath = "/public/rest/api/1.0/execution/"+executionId;
		String cannonicalPath = "PUT&" + relativePath + "&" + "";
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
		String jsonBody = update.getUpdateJson();
		Response r = put(contextPath+relativePath,jsonBody);
		if(!(200<=r.statusCode()&&r.statusCode()<300)) {
			System.out.println("Failed to updated ticket status!");
		}
		return (200<=r.statusCode()&&r.statusCode()<300);
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
	
	private Response put(String resource,Object body) {
		RequestSpecification request = given().baseUri(baseUri);
		if(contentType != null && !contentType.equals("")) {
			request = request.contentType(contentType);
		}
		if(headers != null && headers.size()>0) {
			request = request.headers(headers);
		}
		request.log().all();
		return request.when().body(body).put(resource);
	}
	
	private Response post(String resource, File body) {
		RequestSpecification request = given().baseUri(baseUri);
		if(contentType != null&&!contentType.contentEquals("")) {
			request = request.contentType(contentType);
		}
		if(headers != null && headers.size()>0) {
			request = request.headers(headers);
		}
		request.log().all();
		return request.when().multiPart(body).post(resource).andReturn();
	}
	
	private Response post(String resource, Object body) {
		RequestSpecification request = given().baseUri(baseUri);
		if(contentType != null&&!contentType.contentEquals("")) {
			request = request.contentType(contentType);
		}
		if(headers != null && headers.size()>0) {
			request = request.headers(headers);
		}
		request.log().all();
		return request.when().body(body).post(resource).andReturn();
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