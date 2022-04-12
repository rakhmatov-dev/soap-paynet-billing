package com.venkon.paynet.soap.webservices.soappaynetbilling;

import java.util.Collections;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.venkon.paynet.soap.webservices.soappaynetbilling.billingAPI.BillingAPI;
import com.venkon.paynet.soap.webservices.soappaynetbilling.soap.PaynetBillingEndpoint;

@SpringBootApplication
public class SoapPaynetBillingApplication {

	public static void main(String[] args) {
		// System.out.println("Test POST result: " + BillingAPI.testPOST());
		// System.out.print("JAVA VERSION: " + System.getProperty("java.version"));

		SpringApplication app = new SpringApplication(SoapPaynetBillingApplication.class);

		if (args.length > 0) {
			String serverPort = args[0];
			String wsLogin = args[1];
			String wsPassword = args[2];
			String billingUrl = args[3]; 
			String billingLogin = args[4];
			String billingPassword = args[5];

			PaynetBillingEndpoint.setUSERNAME(wsLogin);
			PaynetBillingEndpoint.setPASSWORD(wsPassword);

			BillingAPI.setURL(billingUrl);
			BillingAPI.setAPI_USERNAME(billingLogin);
			BillingAPI.setAPI_PASSWORD(billingPassword);

			app.setDefaultProperties(Collections.singletonMap("server.port", serverPort));
		}

		app.run(args);

		// SpringApplication.run(SoapPaynetBillingApplication.class, args);
		
	}

}
 