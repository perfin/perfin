package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.dto.PaginatedListWrapper;
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
            		ExchangeRatesProvider.class.getPackage(),
            		PaginatedListWrapper.class.getPackage()).
        		addPackages(true, "org.assertj.core");
    }

    @After
    public void clearRecords() {
        if(currencyManager != null) {
        List<Currency> currencies = currencyManager.getAllCurrencies();
        for(Currency c : currencies) {
        currencyManager.deleteCurrency(c.getId());
            }
        }
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
		assertThat(currencyManager.getAllCurrencies().size()).isEqualTo(0);
	}
	
	@Test
	public void testGetAllCurrencies() {
		Currency currency = currencyManager.createCurrency("AAA", null);
		assertThat(currencyManager.getAllCurrencies()).isNotEmpty().contains(currency);
		assertThat(currencyManager.getAllCurrencies().size()).isEqualTo(1);
	}
	
    @Test
    @RunAsClient
    public void testRest(@ArquillianResource URL base) throws URISyntaxException {
        Currency unstored = new Currency();
        unstored.setCode("QWE");
        
        Entity<Currency> currency = Entity.entity(unstored, MediaType.APPLICATION_JSON);
        
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(base.toURI() + "resources/currencies/");
        Response response = target.request(MediaType.APPLICATION_JSON).post(currency);
        
        Currency stored = response.readEntity(Currency.class);
        assertThat(stored.getId()).isNotNull();
        assertThat(stored.getCode()).isEqualTo(unstored.getCode());
        assertThat(stored.getName()).isEqualTo(unstored.getName());
        
        Response responseGet = target.request(MediaType.APPLICATION_JSON).get();
        PaginatedListWrapper<Currency> wrapper = responseGet.readEntity(PaginatedListWrapper.class);
        assertThat(wrapper.getList().size()).isEqualTo(1);
        
        WebTarget targetDelete = client.target(base.toURI() + "resources/currencies/" 
                + stored.getId() + "/");
        targetDelete.request().delete();
        
        responseGet = target.request(MediaType.APPLICATION_JSON).get();
        wrapper = responseGet.readEntity(PaginatedListWrapper.class);
        assertThat(wrapper.getList().size()).isEqualTo(0);
	    
    }
}
