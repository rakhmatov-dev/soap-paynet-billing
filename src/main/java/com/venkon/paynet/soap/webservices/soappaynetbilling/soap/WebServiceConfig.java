package com.venkon.paynet.soap.webservices.soappaynetbilling.soap;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.XsdSchemaCollection;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

//Enable Spring Web Services
@EnableWs
//Spring Configuration
@Configuration
public class WebServiceConfig {

	@Bean
	ServletRegistrationBean messageDispatcherServlet(ApplicationContext context) {
		MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
		messageDispatcherServlet.setApplicationContext(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
	}
	
	@Bean(name="ProviderWebService")
	public Wsdl11Definition defaultWsdl11Definition (XsdSchema paynetSchema) {
		
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("ProviderWebService.wsdl"));
        return wsdl11Definition;
		
		
		/*
		DefaultWsdl11Definition definition = new DefaultWsdl11Definition();
		definition.setPortTypeName("ProviderWebService");
		definition.setTargetNamespace("http://uws.provider.com/");
		definition.setLocationUri("/ws");
		// definition.setSchema(paynetSchema);
		definition.setRequestSuffix("Arguments");
		definition.setResponseSuffix("Result");
		definition.setSchemaCollection(getXsdSchema());
		return definition; 
		*/
	}  
	
	@Bean
	public XsdSchema paynetSchema() {
		return new SimpleXsdSchema(new ClassPathResource("ProviderWebService.xsd"));
	}
	
}
 