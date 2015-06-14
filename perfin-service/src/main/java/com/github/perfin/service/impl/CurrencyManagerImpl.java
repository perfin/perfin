package com.github.perfin.service.impl;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;

@Stateless
public class CurrencyManagerImpl implements CurrencyManager {
	
	@PersistenceContext(unitName="primary")
	private EntityManager em;

	@Override
	public Currency createCurrency(String code, String name) {
		Currency currency = new Currency();
		currency.setCode(code);
		currency.setName(name);
		
		em.persist(currency);
		
		return currency;
	}

	@Override
	public Currency updateCurrency(Long id, String code, String name) {
		Currency currency = em.find(Currency.class, id);
		
		currency.setCode(code);
		currency.setName(name);
		
		em.merge(currency);
		
		return currency;
	}

	@Override
	public void deleteCurrency(Long id) {
		Currency currency = em.find(Currency.class, id);
		
		em.remove(currency);
		
	}

	@Override
	public List<Currency> getAllCurrencies() {
		Query query = em.createNamedQuery("getAllCurrencies");
		
		List<Currency> currencies = query.getResultList();
		return currencies;
	}

}
