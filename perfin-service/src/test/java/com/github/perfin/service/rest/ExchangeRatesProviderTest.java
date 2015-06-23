package com.github.perfin.service.rest;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.AdminCaller;
import com.github.perfin.service.TestWebArchiveHelper;
import com.github.perfin.service.api.CurrencyManager;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import javax.inject.Inject;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

@RunWith(Arquillian.class)
@Transactional
public class ExchangeRatesProviderTest {

    @Inject
    private ExchangeRatesProvider rates;
    
    @Inject
    private CurrencyManager currencyManager;
    
    @Inject
    private AdminCaller adminCaller;

    @Deployment
    public static Archive<?> getDeployment() {
        return TestWebArchiveHelper.getDeployment();
                    
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBase() throws ExecutionException, InterruptedException {
        BigDecimal btc = rates.getLatestRatio("BTC", "EUR").get();
    }

    @Ignore
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTarget() throws ExecutionException, InterruptedException {
        BigDecimal btc = rates.getLatestRatio("EUR", "BTC").get();
    }

    @Test
    public void testEuroBase() throws ExecutionException, InterruptedException {
        BigDecimal usd = rates.getLatestRatio("EUR", "USD").get();
        Assertions.assertThat(usd).isBetween(BigDecimal.valueOf(0.5), BigDecimal.valueOf(1.5));

        BigDecimal czk = rates.getLatestRatio("EUR", "CZK").get();
        Assertions.assertThat(czk).isBetween(BigDecimal.valueOf(25), BigDecimal.valueOf(30));
    }
    
    @Test
    public void testSaveGetAll() throws Exception {

        adminCaller.call(new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                saveGetAllCallable();
                return null;
            }
        });
        
    }

    private void saveGetAllCallable() {
        Currency eur = new Currency();
        eur.setCode("EUR");
        eur = currencyManager.saveCurrency(eur);
            
        Currency czk = new Currency();
        czk.setCode("CZK");
        czk = currencyManager.saveCurrency(czk);
            
        ExchangeRate er = new ExchangeRate();
        er.setDate(LocalDate.of(2014, 10, 6));
        er.setRatio(BigDecimal.ONE);
        er.setOrigin(eur);
        er.setTarget(czk);
        rates.saveRate(er);
        
        Assertions.assertThat(er.getId()).isNotNull();
        Assertions.assertThat(rates.getStoredRates().size()).isEqualTo(1);
    }

}
