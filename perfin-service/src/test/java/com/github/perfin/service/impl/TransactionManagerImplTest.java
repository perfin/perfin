package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Matchers.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.Transaction;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.TransactionManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@RunWith(Arquillian.class)
@Transactional
public class TransactionManagerImplTest {

    @InjectMocks
    private TransactionManagerImpl transactionManager;
    
    @Mock
    private UserManager userManager;
    
    @Mock
    private ResourceManager resourceManager;
    
    @Spy
    @PersistenceContext(unitName="primary")
    private EntityManager em;
    
    private User user;
    private Currency currency;
    private Resource resource;
    private Category category;
    
    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, 
                    TransactionManager.class.getPackage(), 
                    TransactionManagerImpl.class.getPackage(), 
                    Transaction.class.getPackage(),
                    ExchangeRatesProvider.class.getPackage(),
                    PaginatedListWrapper.class.getPackage()).
                addPackages(true, "org.assertj.core");
        
        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-all").withTransitivity().asFile());
        
        return war;
    }
    
    @Before
    public void initEntities() {
        MockitoAnnotations.initMocks(this);
        
        currency = new Currency();
        currency.setCode("XXX");
        em.persist(currency);
        
        user = new User();
        user.setUserName("john tester");
        user.setDefaultCurrency(currency);
        em.persist(user);
        
        category = new Category();
        category.setName("CommonCategory");
        category.setUser(user);
        em.persist(category);
        
        resource = new Resource();
        resource.setCurrency(currency);
        resource.setInitialBalance(BigDecimal.ZERO);
        resource.setCurrentBalance(BigDecimal.ONE);
        resource.setName("Bank Account");
        resource.setUser(user);
        em.persist(resource);
        
        Mockito.when(userManager.getCurrentUser()).thenReturn(user);
        Mockito.when(resourceManager.saveResource(any(Resource.class)))
            .then(new Answer<Resource>() {

                @Override
                public Resource answer(InvocationOnMock invocation) throws Throwable {
                  Object[] args = invocation.getArguments();
                  return (Resource) args[0];
                }
            });
    }
    
    @Test
    public void testSaveGetDelete() {
        Transaction tran = new Transaction();
        tran.setAmount(BigDecimal.ONE);
        tran.setCategory(category);
        tran.setDate(LocalDate.now());
        tran.setNote("nothing special");
        tran.setResource(resource);
        transactionManager.saveTransaction(tran);
        
        assertThat(tran.getId()).isNotNull();
        
        PaginatedListWrapper<Transaction> trans = transactionManager.getTransactions(1, "id", "asc");
        assertThat(trans.getList().size()).isEqualTo(1);
        assertThat(trans.getList().get(0)).isEqualTo(tran);
        
        transactionManager.deleteTransaction(tran.getId());
        trans = transactionManager.getTransactions(1, "id", "asc");
        assertThat(trans.getList().size()).isEqualTo(0);
    }
}
