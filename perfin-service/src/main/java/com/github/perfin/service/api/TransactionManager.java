package com.github.perfin.service.api;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;

import java.math.BigDecimal;
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
    Transaction createTransaction(Resource resource, Category category, BigDecimal amount, String note);

    /**
     * Creates transaction between resources.
     *
     * @param originalResource resource which balance is decreased
     * @param targetResource   resource which balance is increased
     * @param category         transaction category
     * @param originalAmount   amount in original resource default currency
     * @param targetAmount     amount in target resource default currency
     * @param note             note explaining purpose of transaction (optional)
     * @return new transaction record
     */
    Transaction createTransfer(Resource originalResource, Resource targetResource, Category category,
            BigDecimal originalAmount, BigDecimal targetAmount, String note);

    /**
     * Updates transaction attributes.
     *
     * @param id       transaction ID
     * @param resource new resource, nothing changed if null
     * @param category new category, nothing changed if null
     * @param amount   new amount, nothing changed if null
     * @param note     new note, nothing changed if null
     * @return updated transaction record
     * @throws IllegalArgumentException if id does not exist or it is someone else's transaction
     */
    Transaction updateTransaction(Long id, Resource resource, Category category, BigDecimal amount, String note);

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

}
