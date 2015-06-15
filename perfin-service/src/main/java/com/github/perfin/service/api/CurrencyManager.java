package com.github.perfin.service.api;

import com.github.perfin.model.entity.Currency;

import java.util.List;

/**
 * Manages all operations on currency records. Only administrator can call modifying methods.
 */
public interface CurrencyManager {

    /**
     * Add a new currency to the database.
     *
     * @param code unique currency code
     * @param name long currency name (optional)
     * @return newly added currency record
     * @throws IllegalArgumentException if code doesn't conform one of these:
     * 	- code length equal to 3
     *  - code is uppercase
     *  - code is unique															
     */
    Currency createCurrency(String code, String name);

    /**
     * Update the existing currency record.
     *
     * @param id   id of the existing currency
     * @param code new unique currency code, null or empty otherwise if no change required
     * @param name new currency name, null or empty otherwise if no change required
     * @return updated currency record
     * @throws IllegalArgumentException if id does not exist in database or new code is not unique
     */
    Currency updateCurrency(Long id, String code, String name);

    /**
     * Deletes the existing currency record.
     *
     * @param id id of the existing currency
     * @throws IllegalArgumentException if id does not exist in database
     * @throws IllegalStateException    if the currency cannot be deleted because of references
     */
    void deleteCurrency(Long id);

    /**
     * Gets all currency records.
     *
     * @return list of all currencies
     */
    List<Currency> getAllCurrencies();

}
