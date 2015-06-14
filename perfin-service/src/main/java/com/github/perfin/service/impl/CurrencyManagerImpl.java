package com.github.perfin.service.impl;

import java.util.List;

import javax.ejb.Stateless;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyManager;

@Stateless
public class CurrencyManagerImpl implements CurrencyManager {

	@Override
	public Currency createCurrency(String code, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency updateCurrency(Long id, String code, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCurrency(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Currency> getAllCurrencies() {
		// TODO Auto-generated method stub
		return null;
	}

}
