package com.github.perfin.service.impl;

import java.math.BigDecimal;

import javax.ejb.Stateless;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.api.CurrencyConverter;

@Stateless
public class CurrencyConverterImpl implements CurrencyConverter {

	@Override
	public BigDecimal convert(BigDecimal amount, Currency originalCurrency,
			Currency targetCurrency) {
		// TODO Auto-generated method stub
		return null;
	}

}
