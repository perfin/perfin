package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.*;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@ApplicationPath("/resources")
@Path("currencies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class CurrencyManagerImpl extends Application implements CurrencyManager {

    @PersistenceContext
    private EntityManager em;

    public Currency createCurrency(String code, String name) {
        Currency currency = new Currency();
        currency.setCode(code);
        currency.setName(name);

        em.persist(currency);

        return currency;
    }

    public Currency updateCurrency(Long id, String code, String name) {
        Currency currency = em.find(Currency.class, id);

        currency.setCode(code);
        currency.setName(name);

        em.merge(currency);

        return currency;
    }

    @POST
    @Override
    public Currency saveCurrency(Currency currency) {
        if (currency.getId() == null) {
            return createCurrency(currency.getCode(), currency.getName());
        } else {
            return updateCurrency(currency.getId(), currency.getCode(), currency.getName());
        }
    }

    @DELETE
    @Path("{id}")
    @Override
    public void deleteCurrency(@PathParam("id") Long id) {
        Currency currency = em.find(Currency.class, id);

        em.remove(currency);
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

    public List<Currency> getAllCurrencies() {
        Query query = em.createNamedQuery("getAllCurrencies");

        List<Currency> currencies = query.getResultList();
        return currencies;
    }

    @GET
    @Override
    public PaginatedListWrapper<Currency> getCurrencies(
            @DefaultValue("1") @QueryParam("page") Integer page,
            @DefaultValue("id") @QueryParam("sortFields") String sortFields,
            @DefaultValue("asc") @QueryParam("sortDirections") String sortDirections) {
        PaginatedListWrapper<Currency> paginatedListWrapper = new PaginatedListWrapper<>();
        paginatedListWrapper.setCurrentPage(page);
        paginatedListWrapper.setSortFields(sortFields);
        paginatedListWrapper.setSortDirections(sortDirections);
        paginatedListWrapper.setPageSize(5);
        return findCurrencies(paginatedListWrapper);
    }

}
