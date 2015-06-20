package com.github.perfin.service.rest;

import com.github.perfin.model.entity.Currency;
import org.assertj.core.api.Assertions;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

@RunWith(Arquillian.class)
public class ExchangeRatesProviderTest {

    @Inject
    private ExchangeRatesProvider rates;

    @Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, ExchangeRatesProvider.class.getPackage(), Currency.class.getPackage());
    }

    @Test
    public void testEuroBase() throws ExecutionException, InterruptedException {
        BigDecimal usd = rates.getLatestRatio("EUR", "USD").get();
        Assertions.assertThat(usd).isBetween(BigDecimal.valueOf(0.5), BigDecimal.valueOf(1.5));

        BigDecimal czk = rates.getLatestRatio("EUR", "CZK").get();
        Assertions.assertThat(usd).isBetween(BigDecimal.valueOf(25), BigDecimal.valueOf(30));
    }

}
