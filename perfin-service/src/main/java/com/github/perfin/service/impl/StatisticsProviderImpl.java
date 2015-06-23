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

        Map<Category, List<Transaction>> incomeTransactionsByCategory = groupTransactionsByCategoryAndConvert(defaultCurrency, incomeTransactions);

        ComputeSums computeSums = new ComputeSums(incomeTransactionsByCategory).invoke();
        Map<String, String> totalIncomeByCategory = computeSums.getTotalByCategory();
        BigDecimal totalIncome = computeSums.getTotal();

        //expenses
        query = em.createQuery("SELECT t FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate AND t.amount < 0 AND t.resource.user = :user");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", currentUser);

        List<Transaction> expenseTransactions = query.getResultList();

        Map<Category, List<Transaction>> expenseTransactionsByCategory = groupTransactionsByCategoryAndConvert(defaultCurrency, expenseTransactions);

        computeSums = new ComputeSums(expenseTransactionsByCategory).invoke();
        Map<String, String> totalExpenseByCategory = computeSums.getTotalByCategory();
        BigDecimal totalExpense = computeSums.getTotal();

        Statistics statistics = new Statistics(startDate.toString(), endDate.toString(), totalIncomeByCategory, String.valueOf(totalIncome), totalExpenseByCategory, String.valueOf(totalExpense), defaultCurrency.getCode());

        return statistics;
    }

    private Map<Category, List<Transaction>> groupTransactionsByCategoryAndConvert(Currency defaultCurrency, List<Transaction> incomeTransactions) {
        Map<Category, List<Transaction>> transactionsByCategory = new HashMap<>();

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
            if (transactionsByCategory.containsKey(category)) {
                transactionsByCategory.get(category).add(transactionToAdd);
            } else {
                List<Transaction> list = new ArrayList<>();
                list.add(transactionToAdd);
                transactionsByCategory.put(category, list);
            }
        }
        return transactionsByCategory;
    }

    private class ComputeSums {
        private Map<Category, List<Transaction>> transactionsByCategory;
        private Map<String, String> totalByCategory;
        private BigDecimal total;

        public ComputeSums(Map<Category, List<Transaction>> TransactionsByCategory) {
            this.transactionsByCategory = TransactionsByCategory;
        }

        public Map<String, String> getTotalByCategory() {
            return totalByCategory;
        }

        public BigDecimal getTotal() {
            return total;
        }

        public ComputeSums invoke() {
            totalByCategory = new HashMap<>();
            total = BigDecimal.ZERO;
            total = total.setScale(2, RoundingMode.CEILING);

            for (Map.Entry<Category, List<Transaction>> entry : transactionsByCategory.entrySet()) {
                BigDecimal totalCategory = BigDecimal.ZERO;
                totalCategory = totalCategory.setScale(2, RoundingMode.CEILING);

                for (Transaction trans : entry.getValue()) {
                    BigDecimal amount = trans.getAmount();
                    if (amount.compareTo(BigDecimal.ZERO) == -1) {
                        amount = trans.getAmount().negate();
                    }
                    totalCategory = totalCategory.add(amount);
                    total = total.add(amount);
                }


                this.totalByCategory.put(entry.getKey().getName(), String.valueOf(totalCategory));
            }

            if (total.compareTo(BigDecimal.ZERO) == -1){
                total = total.negate();
            }
            return this;
        }
    }
}
