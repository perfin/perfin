package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.api.StatisticsProvider;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.Statistics;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticsProviderImpl implements StatisticsProvider {

    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserManager userManager;

    @Inject
    private CurrencyConverter currencyConverter;

    @Override
    public Statistics getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) {
        User currentUser = userManager.getCurrentUser();
        Currency defaultCurrency = currentUser.getDefaultCurrency();
        //incomes
        Query query = em.createQuery("SELECT t FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate AND t.amount > 0 AND t.resource.user = :user");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", currentUser);

        List<Transaction> incomeTransactions = query.getResultList();

        Map<Category, List<Transaction>> incomeTransactionsByCategory = new HashMap<>();

        //will group transactions by category with correct currency
        for (Transaction t : incomeTransactions) {
            Transaction transactionToAdd = new Transaction();
            //first, we will convert to default currency
            if (!defaultCurrency.equals(t.getResource().getCurrency())) {
                transactionToAdd.setAmount(currencyConverter.convert(t.getAmount(), t.getResource().getCurrency(), defaultCurrency));
            } else {
                transactionToAdd = t;
            }

            //group transactions by categories
            Category category = t.getCategory();
            if (incomeTransactionsByCategory.containsKey(category)) {
                incomeTransactionsByCategory.get(category).add(transactionToAdd);
            } else {
                List<Transaction> list = new ArrayList<>();
                list.add(transactionToAdd);
                incomeTransactionsByCategory.put(category, list);
            }
        }

        Map<String, String> finalTotalIncomeByCategory = new HashMap<>();
        BigDecimal totalIncome = BigDecimal.ZERO;

        for (Map.Entry<Category, List<Transaction>> entry : incomeTransactionsByCategory.entrySet()) {
            BigDecimal totalIncomeByCategory = BigDecimal.ZERO;

            for (Transaction trans : entry.getValue()) {
                totalIncomeByCategory.add(trans.getAmount());
                totalIncome.add(trans.getAmount());
            }

            totalIncomeByCategory.setScale(2, RoundingMode.CEILING);
            finalTotalIncomeByCategory.put(entry.getKey().getName(), String.valueOf(totalIncomeByCategory));
        }

        //expenses
        query = em.createQuery("SELECT t FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate AND t.amount < 0 AND t.resource.user = :user");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", currentUser);

        List<Transaction> expenseTransactions = query.getResultList();

        Map<Category, List<Transaction>> expenseTransactionsByCategory = new HashMap<>();

        //will group transactions by category with correct currency
        for (Transaction t : expenseTransactions) {
            Transaction transactionToAdd = new Transaction();
            //first, we will convert to default currency
            if (!defaultCurrency.equals(t.getResource().getCurrency())) {
                transactionToAdd.setAmount(currencyConverter.convert(t.getAmount(), t.getResource().getCurrency(), defaultCurrency));
            } else {
                transactionToAdd = t;
            }

            //group transactions by categories
            Category category = t.getCategory();
            if (expenseTransactionsByCategory.containsKey(category)) {
                expenseTransactionsByCategory.get(category).add(transactionToAdd);
            } else {
                List<Transaction> list = new ArrayList<>();
                list.add(transactionToAdd);
                expenseTransactionsByCategory.put(category, list);
            }
        }

        Map<String, String> finalTotalExpenseByCategory = new HashMap<>();
        BigDecimal totalExpense = BigDecimal.ZERO;

        for (Map.Entry<Category, List<Transaction>> entry : expenseTransactionsByCategory.entrySet()) {
            BigDecimal totalExpenseByCategory = BigDecimal.ZERO;

            for (Transaction trans : entry.getValue()) {
                totalExpenseByCategory.add(trans.getAmount());
                totalExpense.add(trans.getAmount());
            }

            totalExpenseByCategory.setScale(2, RoundingMode.CEILING);
            finalTotalExpenseByCategory.put(entry.getKey().getName(), String.valueOf(totalExpenseByCategory));
        }



        Statistics statistics = new Statistics(startDate.toString(), endDate.toString(), finalTotalIncomeByCategory, String.valueOf(totalIncome), finalTotalExpenseByCategory, String.valueOf(totalExpense), defaultCurrency.getCode());

        return statistics;
    }
}
