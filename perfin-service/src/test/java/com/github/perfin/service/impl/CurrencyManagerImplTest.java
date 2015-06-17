package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.*;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@RunWith(Arquillian.class)
public class CurrencyManagerImplTest {

	@Inject
	private CurrencyManager currencyManager;
	
	@Deployment
    public static Archive<?> getDeployment() {
        return ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, 
            		CurrencyManager.class.getPackage(), 
            		CurrencyManagerImpl.class.getPackage(), 
            		Currency.class.getPackage(),
            		ExchangeRatesProvider.class.getPackage()).
        		addPackages(true, "org.assertj.core");
    }
	
	@Test
    public void testCreateCurrency() {
    	Currency currency = currencyManager.createCurrency("ABC", "ABECE");
    	assertThat(currency.getId()).isNotNull();
    }
	
	@Test
    public void testUpdateCurrency() {
    	Currency currency = currencyManager.createCurrency("CDE", "CEDEE");
    	assertThat(currency.getId()).isNotNull();
    	
    	currency = currencyManager.updateCurrency(currency.getId(), "EDC", null);
    	assertThat(currency.getCode()).isEqualTo("EDC");
    	assertThat(currency.getName()).isNull();;
    }
	
	@Test
	public void testDeleteCurrency() {
		Currency currency = currencyManager.createCurrency("XYZ", null);
    	
		currencyManager.deleteCurrency(currency.getId());
		assertThat(currencyManager.getAllCurrencies()).doesNotContain(currency);
	}
	
	@Test
	public void testGetAllCurrencies() {
		Currency currency = currencyManager.createCurrency("AAA", null);
		assertThat(currencyManager.getAllCurrencies()).isNotEmpty().contains(currency);
	}
}
