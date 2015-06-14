package com.github.perfin.service.rest;

import java.math.BigDecimal;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

@Stateless
public class ExchangeRatesProvider {

	@Asynchronous
	public Future<BigDecimal> convertFromTo(String originalCode, String targetCode, BigDecimal value) {
		return new AsyncResult<BigDecimal>(getRate(originalCode, targetCode).multiply(value));
	}
	
	private BigDecimal getRate(String from, String to) {
		
		Client client = ClientBuilder.newClient();
	    WebTarget target = client.target("http://api.fixer.io/latest?base=" + from);
	    JsonObject response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
	    JsonObject rates = response.getJsonObject("rates");
	    JsonNumber number;
	    try{
	    	number = rates.getJsonNumber(to);
	    }catch(NullPointerException ex) {
	    	throw new IllegalArgumentException(to + " is uknown currency shortcut");
	    }
	   
	    return number.bigDecimalValue();
	}
}
