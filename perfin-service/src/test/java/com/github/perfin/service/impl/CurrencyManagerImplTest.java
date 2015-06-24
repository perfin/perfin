package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.TestWebArchiveHelper;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJBException;
import javax.inject.Inject;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Arquillian.class)
public class CurrencyManagerImplTest {

    @Inject
    private CurrencyManager currencyManager;

    @Deployment
    public static Archive<?> getDeployment() {
        return TestWebArchiveHelper.getDeployment();
    }

    @After
    public void clearRecords() {
        if (currencyManager != null) {
            List<Currency> currencies = currencyManager.getCurrencies(1, "id", "asc", false).getList();
            for (Currency c : currencies) {
                currencyManager.deleteCurrency(c.getId());
            }
        }
    }

    @Ignore
    @Test
    public void testSaveGetDelete() {
        Currency currencyOne = new Currency();
        currencyOne.setCode("ABC");
        currencyOne.setName("ABECE");
        currencyOne = currencyManager.saveCurrency(currencyOne);
        assertThat(currencyOne.getId()).isNotNull();

        Currency currencyTwo = new Currency();
        currencyTwo.setCode("DEF");
        currencyTwo.setName("DEEF");
        currencyTwo = currencyManager.saveCurrency(currencyTwo);
        assertThat(currencyTwo.getId()).isNotNull();

        currencyManager.deleteCurrency(currencyOne.getId());
        PaginatedListWrapper<Currency> wrapper = currencyManager.getCurrencies(1, "id", "asc", false);
        assertThat(wrapper.getList().size()).isEqualTo(1);
        assertThat(wrapper.getList().get(0)).isEqualTo(currencyTwo);
    }

    @Ignore
    @Test
    public void testUpdate() {
        Currency currency = new Currency();
        currency.setCode("ABC");
        currency = currencyManager.saveCurrency(currency);

        Currency updated = currency;
        updated.setName("Not Nulll Name");
        updated = currencyManager.saveCurrency(updated);

        PaginatedListWrapper<Currency> wrapper = currencyManager.getCurrencies(1, "id", "asc", false);
        assertThat(wrapper.getList().size()).isEqualTo(1);
        assertThat(wrapper.getList().get(0)).isEqualTo(updated);
    }

    @Ignore
    @Test
    public void testInvalidOperations() {
        // invalid code
        Currency currency = new Currency();
        currency.setCode("aB");
        try {
            currencyManager.saveCurrency(currency);
            fail("Code is not uppercase ond of length 3");
        } catch (EJBException ejbe) {
            assertThat(ejbe.getCause()).isInstanceOf(ConstraintViolationException.class);
        }

        currency.setCode(null);
        try {
            currencyManager.saveCurrency(currency);
            fail("Code is not uppercase ond of length 3");
        } catch (EJBException ejbe) {
            assertThat(ejbe.getCause()).isInstanceOf(ConstraintViolationException.class);
        }

        currency.setCode("XXX");
        currencyManager.saveCurrency(currency);

        Currency nonUnique = new Currency();
        nonUnique.setCode("XXX");
        try {
            currencyManager.saveCurrency(nonUnique);
            fail("stored non unique code");
        } catch (EJBException ejbe) {
            assertThat(ejbe.getCause()).isInstanceOf(PersistenceException.class);
        }

        try {
            currencyManager.deleteCurrency(123456l);
            fail("fail non existing record");
        } catch (EJBException e) {
            //ok
        }
    }

    @Ignore
    @Test
    @RunAsClient
    public void testRest(@ArquillianResource URL base) throws URISyntaxException {
        Currency unstored = new Currency();
        unstored.setCode("QWE");

        Entity<Currency> currency = Entity.entity(unstored, MediaType.APPLICATION_JSON);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(base.toURI() + "service/currencies/");
        Response response = target.request(MediaType.APPLICATION_JSON).post(currency);

        Currency stored = response.readEntity(Currency.class);
        assertThat(stored.getId()).isNotNull();
        assertThat(stored.getCode()).isEqualTo(unstored.getCode());
        assertThat(stored.getName()).isEqualTo(unstored.getName());
        
        WebTarget targetGetById = client.target(base.toURI() + "service/currencies/"
                + stored.getId() + "/");
        Response resGetById = targetGetById.request().get();
        Currency cur = resGetById.readEntity(Currency.class);
        assertThat(cur).isEqualTo(stored);

        Response responseGet = target.request(MediaType.APPLICATION_JSON).get();
        PaginatedListWrapper<Currency> wrapper = responseGet.readEntity(PaginatedListWrapper.class);
        assertThat(wrapper.getList().size()).isEqualTo(1);

        WebTarget targetDelete = client.target(base.toURI() + "service/currencies/"
                + stored.getId() + "/");
        targetDelete.request().delete();

        responseGet = target.request(MediaType.APPLICATION_JSON).get();
        wrapper = responseGet.readEntity(PaginatedListWrapper.class);
        assertThat(wrapper.getList().size()).isEqualTo(0);
    }

}
