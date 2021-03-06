package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Stateless
@ApplicationPath("/service")
@Path("currencies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyManagerImpl extends Application implements CurrencyManager {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserManager userManager;

    private Currency createCurrency(String code, String name) {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setName(name);

        em.persist(currency);

        return currency;
    }

    private Currency updateCurrency(Long id, String code, String name) {
        Currency currency = em.find(Currency.class, id);

        currency.setCode(code);
        currency.setName(name);

        em.merge(currency);

        return currency;
    }

    @RolesAllowed("admin")
    @POST
    @Override
    public Currency saveCurrency(Currency currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Null instance can't be saved");
        }
        try {
            if (currency.getId() == null) {
                return createCurrency(currency.getCode(), currency.getName());
            } else {
                return updateCurrency(currency.getId(), currency.getCode(), currency.getName());
            }
        } catch (EJBTransactionRolledbackException e) {
            throw new IllegalArgumentException("Unabe to save currency: " + currency, e);
        }
    }

    @RolesAllowed("admin")
    @DELETE
    @Path("{id}")
    @Override
    public void deleteCurrency(@PathParam("id") Long id) {
        Currency currency = em.find(Currency.class, id);

        em.remove(currency);
    }

    @PermitAll
    @GET
    @Path("{id}")
    @Override
    public Currency getCurrency(@PathParam("id") Long id) {
        return em.find(Currency.class, id);
    }

    private Integer countCurrencies() {
        Query query = em.createQuery("SELECT COUNT(c.id) FROM Currency c");
        return ((Long) query.getSingleResult()).intValue();
    }

    private List<Currency> findCurrencies(int startPosition, int maxResults, String sortFields, String sortDirections) {
        Query query = em.createQuery("SELECT c FROM Currency c ORDER BY " + sortFields + " " + sortDirections);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResults);
        return query.getResultList();
    }

    private PaginatedListWrapper<Currency> findCurrencies(PaginatedListWrapper<Currency> wrapper) {
        wrapper.setTotalResults(countCurrencies());
        int start = (wrapper.getCurrentPage() - 1) * wrapper.getPageSize();
        wrapper.setList(findCurrencies(start, wrapper.getPageSize(), wrapper.getSortFields(),
                wrapper.getSortDirections()));
        return wrapper;
    }

    @PermitAll
    @GET
    @Override
    public PaginatedListWrapper<Currency> getCurrencies(
            @DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("id") @QueryParam("sortFields") String sortFields,
            @DefaultValue("asc") @QueryParam("sortDirections") String sortDirections,
            @DefaultValue("false") @QueryParam("all") Boolean all) {
        PaginatedListWrapper<Currency> paginatedListWrapper = new PaginatedListWrapper<>();

        if (all) {
            if (userManager.isInRole("standard").get("standard")) {
                List<Currency> currencies = new ArrayList<>();
                currencies.add(userManager.getCurrentUser().getDefaultCurrency());
                paginatedListWrapper.setList(currencies);
                return paginatedListWrapper;
            }
            Query query = em.createQuery("SELECT c FROM Currency c", Currency.class);
            paginatedListWrapper.setList(query.getResultList());
            return paginatedListWrapper;
        }

        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(10);
        return findCurrencies(paginatedListWrapper);
    }

    @PermitAll
    @Override
    public List<Currency> getCurrencies() {
        Query query = em.createNamedQuery("getAllCurrencies");
        return query.getResultList();
    }

}
