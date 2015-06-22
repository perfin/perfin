package com.github.perfin.service.impl;

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
import java.time.LocalDate;
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
        //incomes
        Query query = em.createQuery("SELECT T FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate AND t.amount > 0 AND t.resource.user = :user");
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        query.setParameter("user", currentUser);

        List<Transaction> incomeTransactions = query.getResultList();

        List<Object[]> results = query.getResultList();
        Map<String, String> incomes = new HashMap<>();

        for (Object[] row : results) {
            incomes.put(row[0].toString(), row[1].toString());
        }

        //total income

        BigDecimal totalIncome = new BigDecimal(0);

        for (Transaction t : incomeTransactions) {
            if (!currentUser.getDefaultCurrency().equals(t.getResource().getCurrency())) {
                totalIncome = totalIncome.add(currencyConverter.convert(t.getAmount(), t.getResource().getCurrency(), currentUser.getDefaultCurrency()));
            } else {
                totalIncome = totalIncome.add(t.getAmount());
            }

        }


        Statistics statistics = null;

        return statistics;
    }
}
