package com.github.perfin.service.rest;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.concurrent.Future;

@Stateless
public class ExchangeRatesProvider {

    @Asynchronous
    public Future<BigDecimal> getLatestRatio(String from, String to) {
        return new AsyncResult<BigDecimal>(getRate(from, to));
    }

    private BigDecimal getRate(String from, String to) {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://api.fixer.io/latest?base=" + from);
        JsonObject response = target.request(MediaType.APPLICATION_JSON).get(JsonObject.class);
        JsonObject rates = response.getJsonObject("rates");
        JsonNumber number;
        try {
            number = rates.getJsonNumber(to);
        } catch (NullPointerException ex) {
            throw new IllegalArgumentException(to + " is uknown currency shortcut");
        }

        return number.bigDecimalValue();
    }

}
