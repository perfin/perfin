package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.rest.ExchangeRatesProvider;

import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.ExecutionException;

@Stateless
@PermitAll
public class CurrencyConverterImpl implements CurrencyConverter {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private ExchangeRatesProvider exchangeRatesProvider;

    @Override
    public BigDecimal convert(BigDecimal amount, Currency originalCurrency, Currency targetCurrency) {
        Query query = em.createQuery("SELECT er.ratio FROM ExchangeRate er WHERE er.origin.code = :origin AND " +
                "er.target.code = :target AND er.date = :date", BigDecimal.class);
        query.setParameter("origin", originalCurrency.getCode());
        query.setParameter("target", targetCurrency.getCode());
        query.setParameter("date", LocalDate.now());

        BigDecimal ratio = null;
        try {
            ratio = (BigDecimal) query.getSingleResult();
        } catch (NoResultException ex) {
            try {
                ratio = exchangeRatesProvider.getLatestRatio(originalCurrency.getCode(), targetCurrency.getCode()).get();

                ExchangeRate exchangeRate = new ExchangeRate();
                exchangeRate.setOrigin(originalCurrency);
                exchangeRate.setTarget(targetCurrency);
                exchangeRate.setRatio(ratio);
                exchangeRate.setDate(LocalDate.now());
                em.persist(exchangeRate);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        return amount.multiply(ratio);
    }

}
