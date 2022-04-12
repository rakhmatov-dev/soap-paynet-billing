package com.venkon.paynet.soap.webservices.soappaynetbilling.soap;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.json.simple.JSONObject;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.provider.uws.CancelTransactionArguments;
import com.provider.uws.CancelTransactionResult;
import com.provider.uws.ChangePasswordArguments;
import com.provider.uws.ChangePasswordResult;
import com.provider.uws.CheckTransactionArguments;
import com.provider.uws.CheckTransactionResult;
import com.provider.uws.GenericParam;
import com.provider.uws.GetInformationArguments;
import com.provider.uws.GetInformationResult;
import com.provider.uws.GetStatementArguments;
import com.provider.uws.GetStatementResult;
import com.provider.uws.ObjectFactory;
import com.provider.uws.PerformTransactionArguments;
import com.provider.uws.PerformTransactionResult;
import com.provider.uws.TransactionStatement;
import com.venkon.paynet.soap.webservices.soappaynetbilling.billingAPI.BillingAPI;
import com.venkon.paynet.soap.webservices.soappaynetbilling.soap.exceptions.PreconditionFailedException;

@Endpoint
public class PaynetBillingEndpoint {
	
	private static String USERNAME;
	private static String PASSWORD;

	public static void setUSERNAME(String uSERNAME) {
		USERNAME = uSERNAME;
	}

	public static void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "PerformTransactionArguments")
	@ResponsePayload
	public JAXBElement<PerformTransactionResult> processPerformTransactionRequest(@RequestPayload JAXBElement<PerformTransactionArguments> request) {

		// 1. Authentication
		PerformTransactionArguments performTransactionArguments = request.getValue();
		String username = performTransactionArguments.getUsername();
		String password = performTransactionArguments.getPassword();
		checkAuth(username, password);
		//
		
		// 2. Request fields
		long amount = performTransactionArguments.getAmount();
		long serviceId = performTransactionArguments.getServiceId();
		long transactionId = performTransactionArguments.getTransactionId();
		String transactionTime = performTransactionArguments.getTransactionTime();
		List<GenericParam> parameters = performTransactionArguments.getParameters();
		List<Map<String, String>> params = convertParamsToListOfMaps(parameters);
		//
		
		// 3. Build JSONObject for request to Billing
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "PerformTransaction");
		requestBody.put("amount", amount);
		requestBody.put("serviceId", serviceId);
		requestBody.put("transactionId", transactionId);
		requestBody.put("transactionTime", transactionTime);
		requestBody.put("parameters", params);
		JSONObject requestJsonObject = new JSONObject(requestBody);
		//
		  
		// 4. POST request to the Billing
		JSONObject responseBody = BillingAPI.makePost(requestJsonObject);
		//
		 
		// 5. Billing response fields
		PerformTransactionResult performTransactionResult = new PerformTransactionResult();
		performTransactionResult.setErrorMsg((String)responseBody.get("errorMsg"));
		performTransactionResult.setStatus(((Number)responseBody.get("status")).intValue());
		performTransactionResult.setProviderTrnId(((Number)responseBody.get("providerTrnId")).longValue());
		performTransactionResult.setTimeStamp((String)responseBody.get("timeStamp"));
		List<GenericParam> genericParams = convertListOfMapsToParams((List)responseBody.get("parameters"));
		performTransactionResult.setParameters(genericParams);
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<PerformTransactionResult> response = factory.createPerformTransactionResult(performTransactionResult);
		return response;
		//
		
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "CheckTransactionArguments")
	@ResponsePayload
	public JAXBElement<CheckTransactionResult> processCheckTransactionRequest(@RequestPayload JAXBElement<CheckTransactionArguments> request) {
				
		// 1. Authentication
		CheckTransactionArguments checkTransactionArguments = request.getValue();
		String username = checkTransactionArguments.getUsername();
		String password = checkTransactionArguments.getPassword();
		checkAuth(username, password);
		//
		
		// 2. Request fields
		long serviceId = checkTransactionArguments.getServiceId();
		long transactionId = checkTransactionArguments.getTransactionId();
		String transactionTime = checkTransactionArguments.getTransactionTime();
		List<GenericParam> parameters = checkTransactionArguments.getParameters();
		List<Map<String, String>> params = convertParamsToListOfMaps(parameters);
		//

		// 3. Build JSONObject for request to Billing
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "CheckTransaction");
		requestBody.put("serviceId", serviceId);
		requestBody.put("transactionId", transactionId);
		requestBody.put("transactionTime", transactionTime);
		requestBody.put("parameters", params);
		JSONObject requestJsonObject = new JSONObject(requestBody);
		//		
		
		// 4. POST request to the Billing
		JSONObject responseBody = BillingAPI.makePost(requestJsonObject);
		//
		
		// 5. Billing response fields
		CheckTransactionResult checkTransactionResult = new CheckTransactionResult();
		checkTransactionResult.setErrorMsg((String)responseBody.get("errorMsg"));
		checkTransactionResult.setStatus(((Number)responseBody.get("status")).intValue());
		checkTransactionResult.setTimeStamp((String)responseBody.get("timeStamp"));
		checkTransactionResult.setProviderTrnId(((Number)responseBody.get("providerTrnId")).longValue());
		checkTransactionResult.setTransactionState(((Number)responseBody.get("transactionState")).intValue());
		checkTransactionResult.setTransactionStateErrorStatus(((Number)responseBody.get("transactionStateErrorStatus")).intValue());
		checkTransactionResult.setTransactionStateErrorMsg((String)responseBody.get("transactionStateErrorMsg"));
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<CheckTransactionResult> response = factory.createCheckTransactionResult(checkTransactionResult);
		return response;
		//
		
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "CancelTransactionArguments")
	@ResponsePayload
	public JAXBElement<CancelTransactionResult> processCancelTransactionRequest(@RequestPayload JAXBElement<CancelTransactionArguments> request) {

		// 1. Authentication
		CancelTransactionArguments cancelTransactionArguments = request.getValue();
		String username = cancelTransactionArguments.getUsername();
		String password = cancelTransactionArguments.getPassword();
		checkAuth(username, password);
		//		
		
		// 2. Request fields
		long serviceId = cancelTransactionArguments.getServiceId();
		long transactionId = cancelTransactionArguments.getTransactionId();
		List<GenericParam> parameters = cancelTransactionArguments.getParameters();
		List<Map<String, String>> params = convertParamsToListOfMaps(parameters);
		//		

		// 3. Build JSONObject for request to Billing
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "CancelTransaction");
		requestBody.put("serviceId", serviceId);
		requestBody.put("transactionId", transactionId);
		requestBody.put("parameters", params);
		JSONObject requestJsonObject = new JSONObject(requestBody);
		//		
		
		// 4. POST request to the Billing
		JSONObject responseBody = BillingAPI.makePost(requestJsonObject);
		//		
		
		// 5. Billing response fields
		CancelTransactionResult cancelTransactionResult = new CancelTransactionResult();
		cancelTransactionResult.setErrorMsg((String)responseBody.get("errorMsg"));
		cancelTransactionResult.setStatus(((Number)responseBody.get("status")).intValue());
		cancelTransactionResult.setTimeStamp((String)responseBody.get("timeStamp"));
		cancelTransactionResult.setTransactionState(((Number)responseBody.get("transactionState")).intValue());
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<CancelTransactionResult> response = factory.createCancelTransactionResult(cancelTransactionResult);
		return response;
		//
			
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "GetStatementArguments")
	@ResponsePayload
	public JAXBElement<GetStatementResult> processGetStatementRequest(@RequestPayload JAXBElement<GetStatementArguments> request) {

		// 1. Authentication
		GetStatementArguments getStatementArguments = request.getValue();
		String username = getStatementArguments.getUsername();
		String password = getStatementArguments.getPassword();
		checkAuth(username, password);
		//		
		
		// 2. Request fields
		long serviceId = getStatementArguments.getServiceId();
		boolean isOnlyTransactionId = getStatementArguments.isOnlyTransactionId();
		String dateFrom = getStatementArguments.getDateFrom();
		String dateTo = getStatementArguments.getDateTo();
		List<GenericParam> parameters = getStatementArguments.getParameters();
		List<Map<String, String>> params = convertParamsToListOfMaps(parameters);
		//		
		
		// 3. Build JSONObject for request to Billing
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "GetStatement");
		requestBody.put("serviceId", serviceId);
		requestBody.put("isOnlyTransactionId", isOnlyTransactionId);
		requestBody.put("dateFrom", dateFrom);
		requestBody.put("dateTo", dateTo);
		requestBody.put("parameters", params);
		JSONObject requestJsonObject = new JSONObject(requestBody);
		//		
		
		// 4. POST request to the Billing
		JSONObject responseBody = BillingAPI.makePost(requestJsonObject);
		//		
		
		// 5. Billing response fields
		GetStatementResult getStatementResult = new GetStatementResult();
		getStatementResult.setErrorMsg((String)responseBody.get("errorMsg"));
		getStatementResult.setStatus(((Number)responseBody.get("status")).intValue());
		getStatementResult.setTimeStamp((String)responseBody.get("timeStamp"));
		List<TransactionStatement> transactionStatements = convertListOfMapsToStatements((List)responseBody.get("statements"));
		getStatementResult.setStatements(transactionStatements);
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<GetStatementResult> response = factory.createGetStatementResult(getStatementResult);
		return response;
		//
				
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "GetInformationArguments")
	@ResponsePayload
	public JAXBElement<GetInformationResult> processGetInformationRequest(@RequestPayload JAXBElement<GetInformationArguments> request) {
		
		// 1. Authentication
		GetInformationArguments getInformationArguments = request.getValue();
		String username = getInformationArguments.getUsername();
		String password = getInformationArguments.getPassword();
		checkAuth(username, password);
		//		
		
		// 2. Request fields
		long serviceId = getInformationArguments.getServiceId();
		List<GenericParam> parameters = getInformationArguments.getParameters();
		List<Map<String, String>> params = convertParamsToListOfMaps(parameters);
		//		
		
		// 3. Build JSONObject for request to Billing
		HashMap<String,Object> requestBody = new HashMap<String,Object>();
		requestBody.put("method", "GetInformation");
		requestBody.put("serviceId", serviceId);
		requestBody.put("parameters", params);
		JSONObject requestJsonObject = new JSONObject(requestBody);
		//		
		
		// 4. POST request to the Billing
		JSONObject responseBody = BillingAPI.makePost(requestJsonObject);
		//		
		
		// 5. Billing response fields
		GetInformationResult getInformationResult = new GetInformationResult();
		getInformationResult.setErrorMsg((String)responseBody.get("errorMsg"));
		getInformationResult.setStatus(((Number)responseBody.get("status")).intValue());
		getInformationResult.setTimeStamp((String)responseBody.get("timeStamp"));
		List<GenericParam> genericParams = convertListOfMapsToParams((List)responseBody.get("parameters"));
		getInformationResult.setParameters(genericParams);
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<GetInformationResult> response = factory.createGetInformationResult(getInformationResult);
		return response;
		//
	
	}

	@PayloadRoot(namespace = "http://uws.provider.com/", localPart = "ChangePasswordArguments")
	@ResponsePayload
	public JAXBElement<ChangePasswordResult> processChangePasswordRequest(@RequestPayload JAXBElement<ChangePasswordArguments> request) {

		// 1. Authentication
		ChangePasswordArguments changePasswordArguments = request.getValue();
		String username = changePasswordArguments.getUsername();
		String password = changePasswordArguments.getPassword();
		checkAuth(username, password);
		//		

		// 2. Request fields
		String newPassword = changePasswordArguments.getNewPassword();
		//		
		
		PASSWORD = newPassword;
		
		// 5. Billing response fields
		ChangePasswordResult changePasswordResult = new ChangePasswordResult();
		changePasswordResult.setErrorMsg("");
		changePasswordResult.setStatus(1);
		changePasswordResult.setTimeStamp("");
		// 
		
		// 6. Send response
		ObjectFactory factory = new ObjectFactory();
		JAXBElement<ChangePasswordResult> response = factory.createChangePasswordResult(changePasswordResult);
		return response;
		//
		
	}
	
	private void checkAuth(String username, String password) {

		if (!username.equals(USERNAME) || !password.equals(PASSWORD)) {
			throw new PreconditionFailedException();
		}

	}
	
	public List<Map<String, String>> convertParamsToListOfMaps(List<GenericParam> parameters) {
		
		List<Map<String, String>> mapList = new ArrayList<Map<String, String>>();
		
		for(GenericParam param: parameters) {
			
			Map<String, String> map = new HashMap<String, String>();
			map.put(param.getParamKey(), param.getParamValue());
			mapList.add(map);
				
		}
		
		return mapList;
		
	}
	
	public List<GenericParam> convertListOfMapsToParams(List<Map<String, String>> mapList) {
		
		List<GenericParam> parameters = new ArrayList<GenericParam>();

		for (Map<String, String> map : mapList) {
			for (Map.Entry<String, String> entry : map.entrySet()) {

				GenericParam genericParam = new GenericParam();
				genericParam.setParamKey(entry.getKey());
				genericParam.setParamValue(entry.getValue());

				parameters.add(genericParam);

			}
		}
		
		return parameters;
		
	}
	
	public List<TransactionStatement> convertListOfMapsToStatements(List<Map<String, String>> mapList) {
		
		List<TransactionStatement> statements = new ArrayList<TransactionStatement>();

		for (Map map : mapList) {
			
			TransactionStatement statement = new TransactionStatement();
			statement.setAmount(((Number)map.get("amount")).longValue());
			statement.setProviderTrnId(((Number)map.get("providerTrnId")).longValue());
			statement.setTransactionId(((Number)map.get("transactionId")).longValue());
			statement.setTransactionTime((String)map.get("transactionTime"));

			statements.add(statement);
		}
		
		return statements;
		
	}

}
