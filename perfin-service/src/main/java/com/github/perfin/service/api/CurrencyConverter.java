package com.github.perfin.service.api;

import com.github.perfin.model.entity.Currency;

import java.math.BigDecimal;

/**
 * Provides conversion from one currency to another. Exchange rate record is searched in database. If it is not found,
 * information is get from external exchange rate provider through REST interface and it is stored in the database.
 */
public interface CurrencyConverter {

    /**
     * Converts the given amount in one currency to another one.
     *
     * @param amount           amount in original currency
     * @param originalCurrency original currency
     * @param targetCurrency   target currency
     * @return amount in target currency rounded on two decimal points
     * @throws IllegalArgumentException if amount is negative
     */
    BigDecimal convert(BigDecimal amount, Currency originalCurrency, Currency targetCurrency);

}
