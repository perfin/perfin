package com.github.perfin.service.api;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.service.dto.PaginatedListWrapper;

import java.time.LocalDate;
import java.util.List;

/**
 * Manages finance transactions.
 */
public interface TransactionManager {

    /**
     * Creates transaction with external entities.
     *
     * @param resource resource which balance is modified
     * @param category transaction category
     * @param amount   amount in resource default currency
     * @param note     note explaining purpose of transaction (optional)
     * @return new transaction record
     */
    Transaction saveTransaction(Transaction transaction);

    /**
     * Deletes the transaction with the given ID. If it is transfer, associated transaction is also deleted.
     *
     * @param id transaction ID
     */
    void deleteTransaction(Long id);

    /**
     * Gets list of transactions (for current user) within the given date range.
     *
     * @param startDate first day of the date range
     * @param endDate   last day of the date range
     * @return list of transactions within the given date range.
     */
    List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * Gets list of transactions (for current user) in the given category.
     *
     * @param category transaction category
     * @return list of transaction in the given category
     */
    List<Transaction> getTransactionsByCategory(Category category);

    /**
     * Gets list of transactions (for current user) with the given resource
     *
     * @param resource money resource
     * @return list of transactions with the given resource
     */
    List<Transaction> getTransactionsByResource(Resource resource);
    
    PaginatedListWrapper<Transaction> getTransactions(Integer page, String sortFields, String sortDirections);
    
    Transaction getTransaction(Long id);

}
