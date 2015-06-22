package com.github.perfin.service.rest;

import javax.annotation.security.PermitAll;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.github.perfin.model.entity.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Future;

@Stateless
@PermitAll
public class ExchangeRatesProvider {

    @PersistenceContext
    private EntityManager em;
    
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

    public List<ExchangeRate> getStoredRates(){
        Query query = em.createNamedQuery("getAllRates");
        return query.getResultList();
    }
    
    public void saveRate(ExchangeRate rate) {
        if(rate.getId() == null) {
            em.persist(rate);
        } else {
            ExchangeRate old = em.find(ExchangeRate.class, rate.getId());
            old.setDate(rate.getDate());
            old.setRatio(rate.getRatio());
            em.merge(old);
        }
    }
}
