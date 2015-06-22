package com.github.perfin.service.impl;

import com.github.perfin.model.entity.*;
import com.github.perfin.service.api.CurrencyConverter;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.api.StatisticsProvider;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.rest.ExchangeRatesProvider;
import junit.framework.TestCase;
import org.mockito.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;

public class StatisticsProviderImplTest extends TestCase {

    @InjectMocks
    StatisticsProvider statisticsProvider;

    @Spy
    @PersistenceContext
    private EntityManager em;

    @Inject
    private UserManager userManager;

    @Inject
    private CurrencyManager currencyManager;

    @Inject
    private CurrencyConverter currencyConverter;

    @Inject
    private ExchangeRatesProvider exchangeRatesProvider;

    private Currency EUR;

    private Currency USD;

    private User user;

    public void setUp() throws Exception {
        EUR = new Currency();
        EUR.setCode("EUR");
        EUR.setName("Euro");
        em.persist(EUR);

        USD = new Currency();
        USD.setCode("USD");
        USD.setName("Dollar");
        em.persist(USD);

        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setDate(LocalDate.now());
        exchangeRate.setOrigin(USD);
        exchangeRate.setTarget(EUR);
        exchangeRate.setRatio(new BigDecimal("0.8"));


        user = new User();
        user.setUserName("john tester");
        user.setDefaultCurrency(EUR);
        em.persist(user);



        //MockitoAnnotations.initMocks(this);
        Mockito.when(userManager.getCurrentUser()).thenReturn(user);

        Category cars = new Category();
        cars.setName("Cars");
        cars.setUser(user);
        em.persist(cars);

        Resource bankAccount = new Resource();
        bankAccount.setCurrency(USD);
        bankAccount.setBalance(BigDecimal.ZERO);
        bankAccount.setName("Bank Account");
        bankAccount.setUser(user);
        em.persist(bankAccount);

        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal(1000));
        t1.setCategory(cars);
        t1.setResource(bankAccount);

        Transaction t2 = new Transaction();
        t2.setAmount(new BigDecimal(750));
        t2.setCategory(cars);
        t2.setResource(bankAccount);


        User anotherUser = new User();
        anotherUser.setUserName("mark tester");
        anotherUser.setDefaultCurrency(USD);
        em.persist(anotherUser);

    }

    public void testGetStatisticsByDateRange() throws Exception {

    }
}