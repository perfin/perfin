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

    @Mock
    private UserManager userManager;

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

        Category household = new Category();
        household.setName("Household");
        household.setUser(user);
        em.persist(household);

        Resource bankAccount = new Resource();
        bankAccount.setCurrency(USD);
        bankAccount.setBalance(BigDecimal.ZERO);
        bankAccount.setName("Bank Account");
        bankAccount.setUser(user);
        em.persist(bankAccount);

        Resource bankLoan = new Resource();
        bankLoan.setCurrency(USD);
        bankLoan.setBalance(BigDecimal.ZERO);
        bankLoan.setName("Bank Loan");
        bankLoan.setUser(user);
        em.persist(bankLoan);

        Transaction t1 = new Transaction();
        t1.setAmount(new BigDecimal(1000));
        t1.setCategory(cars);
        t1.setResource(bankAccount);
        t1.setDate(LocalDate.now());
        em.persist(t1);

        Transaction t2 = new Transaction();
        t2.setAmount(new BigDecimal(-750));
        t2.setCategory(cars);
        t2.setResource(bankLoan);
        t2.setDate(LocalDate.of(1994, 2, 28));
        em.persist(t2);

        Transaction t3 = new Transaction();
        t3.setAmount(new BigDecimal(-450));
        t3.setCategory(household);
        t3.setResource(bankAccount);
        t3.setDate(LocalDate.now());
        em.persist(t3);

        //------- 2nd User -------

        User anotherUser = new User();
        anotherUser.setUserName("mark tester");
        anotherUser.setDefaultCurrency(USD);
        em.persist(anotherUser);

        Category cars2 = new Category();
        cars2.setName("Cars");
        cars2.setUser(user);
        em.persist(cars2);

        Category household2 = new Category();
        household2.setName("Household");
        household2.setUser(user);
        em.persist(household2);

        Resource bankAccount2 = new Resource();
        bankAccount2.setCurrency(USD);
        bankAccount2.setBalance(BigDecimal.ZERO);
        bankAccount2.setName("Bank Account2");
        bankAccount2.setUser(anotherUser);
        em.persist(bankAccount2);

        Resource bankLoan2 = new Resource();
        bankLoan2.setCurrency(USD);
        bankLoan2.setBalance(BigDecimal.ZERO);
        bankLoan2.setName("Bank Loan2");
        bankLoan2.setUser(anotherUser);
        em.persist(bankLoan2);

        Transaction t4 = new Transaction();
        t4.setAmount(new BigDecimal(550));
        t4.setCategory(cars2);
        t4.setResource(bankAccount2);
        t4.setDate(LocalDate.now());
        em.persist(t4);

        Transaction t5 = new Transaction();
        t5.setAmount(new BigDecimal(-250));
        t5.setCategory(cars2);
        t5.setResource(bankLoan2);
        t5.setDate(LocalDate.of(1992, 3, 18));
        em.persist(t5);

        Transaction t6 = new Transaction();
        t6.setAmount(new BigDecimal(-350));
        t6.setCategory(household2);
        t6.setResource(bankAccount2);
        t6.setDate(LocalDate.now());
        em.persist(t6);

    }

    public void testGetStatisticsByDateRange() throws Exception {

    }
}