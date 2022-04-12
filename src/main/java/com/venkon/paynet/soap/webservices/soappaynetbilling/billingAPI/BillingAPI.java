package com.venkon.paynet.soap.webservices.soappaynetbilling.billingAPI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;

import org.json.simple.JSONObject;

public class BillingAPI {
	
	private static String URL;
	private static String API_USERNAME;
	private static String API_PASSWORD;
	
	public static void setURL(String uRL) {
		URL = uRL;
	}

	public static void setAPI_USERNAME(String aPI_USERNAME) {
		API_USERNAME = aPI_USERNAME;
	}

	public static void setAPI_PASSWORD(String aPI_PASSWORD) {
		API_PASSWORD = aPI_PASSWORD;
	}

	public static String testPOST() {
		
		RestTemplate restTemplate = new RestTemplate();
		
		// <authorization 
		String auth = API_USERNAME + ":" + API_PASSWORD;
		byte[] authCredsBytes = auth.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(authCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		// authorization>
		
		// <request body
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "Test");
		JSONObject requestJsonObject = new JSONObject(requestBody);
		// request body>
	    
		HttpEntity<String> request = 
			      new HttpEntity<String>(requestJsonObject.toString(), headers);
		
		JSONObject responseJsonObject = 
			      restTemplate.postForObject(URL, request, JSONObject.class);
		
		return responseJsonObject.toJSONString();
		
	}
	
	public static JSONObject makePost(JSONObject requestBody) {
		
		RestTemplate restTemplate = new RestTemplate();
		
		String auth = API_USERNAME + ":" + API_PASSWORD;
		byte[] authCredsBytes = auth.getBytes();
		byte[] base64CredsBytes = Base64.getEncoder().encode(authCredsBytes);
		String base64Creds = new String(base64CredsBytes);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		HttpEntity<String> request = 
			      new HttpEntity<String>(requestBody.toString(), headers);
		
		JSONObject responseJsonObject = 
			      restTemplate.postForObject(URL, request, JSONObject.class);
		
		return responseJsonObject;
		
	}
		
}
