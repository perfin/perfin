package com.github.perfin.service.api;

import java.util.List;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.service.dto.PaginatedListWrapper;

/**
 * Manages all operations on currency records. Only administrator can call modifying methods.
 */
public interface CurrencyManager {

    /**
     * Create a new currency or update the existing one
     *
     * @param currency
     * @return persisted currency
     * @throws IllegalArgumentException if code doesn't conform one of these:
     *                                  - code length equal to 3
     *                                  - code is uppercase
     *                                  - code is unique
     */
    Currency saveCurrency(Currency currency);

    /**
     * Deletes the existing currency record.
     *
     * @param id id of the existing currency
     * @throws IllegalArgumentException if id does not exist in database
     * @throws IllegalStateException    if the currency cannot be deleted because of references
     */
    void deleteCurrency(Long id);

    /**
     * Gets currencies
     *
     * @param page
     * @param sortFields
     * @param sortDirections
     * @param all if true, all records are returned regardless the pagination options, false by default
     * @return one page of currency records
     */
    PaginatedListWrapper<Currency> getCurrencies(Integer page, String sortFields, String sortDirections, Boolean all);
    
    Currency getCurrency(Long id);
    
    List<Currency> getCurrencies();

}
