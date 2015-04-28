package cz.muni.fi.pv243.perfin.service.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import cz.muni.fi.pv243.perfin.service.ExchangeRatesProvider;
import cz.muni.fi.pv243.perfin.service.util.ExchangeCurrency;

@RunWith(Arquillian.class)
public class ExchangeRatesProviderTest {

	@Inject
	private ExchangeRatesProvider rates;
	
	@Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addPackages(true, ExchangeRatesProvider.class.getPackage());
    }

    @Test
    public void testExchangeRates() throws InterruptedException, ExecutionException {
        BigDecimal result = null;
        result = rates.convertFromTo(ExchangeCurrency.Usd, ExchangeCurrency.Eur, BigDecimal.ONE).get();
        assertTrue(BigDecimal.ONE.compareTo(result) > 0);
        
        result =  rates.convertFromTo(ExchangeCurrency.Usd, ExchangeCurrency.Eur, BigDecimal.valueOf(100)).get();
        assertTrue(BigDecimal.valueOf(85).compareTo(result) < 0);
    }
}
