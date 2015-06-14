package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@Stateless
public class CurrencyConverterImpl implements CurrencyConverter {
	
	@Inject
	private ExchangeRatesProvider exchangeRatesProvider;
	

	@Override
	public BigDecimal convert(BigDecimal amount, Currency originalCurrency,
			Currency targetCurrency) {
		
		BigDecimal result = BigDecimal.ONE;
		
		try {
			result = exchangeRatesProvider.convertFromTo(originalCurrency.getCode(), targetCurrency.getCode(), amount).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

}
