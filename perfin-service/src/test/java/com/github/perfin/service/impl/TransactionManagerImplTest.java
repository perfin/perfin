package com.github.perfin.service.impl;

import com.github.perfin.model.entity.*;
import com.github.perfin.service.TestWebArchiveHelper;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;

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
    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    private User user;
    private Currency currency;
    private Resource resource;
    private Category category;

    @Deployment
    public static Archive<?> getDeployment() {
        return TestWebArchiveHelper.getDeployment();
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
        resource.setBalance(BigDecimal.ONE);
        resource.setName("Bank Account");
        resource.setUser(user);
        em.persist(resource);

        Mockito.when(userManager.getCurrentUser()).thenReturn(user);
        Mockito.when(resourceManager.saveResource(any(Resource.class)))
                .then(new Answer<Resource>() {

                    @Override
                    public Resource answer(InvocationOnMock invocation) throws Throwable {
                        Object[] args = invocation.getArguments();
                        Resource res = (Resource) args[0];
                        Resource old = em.find(Resource.class, res.getId());
                        old.setBalance(res.getBalance());
                        em.persist(old);
                        
                        return old;
                    }
                });
        Mockito.when(resourceManager.getResource(any(Long.class)))
            .then(new Answer<Resource>() {

                @Override
                public Resource answer(InvocationOnMock invocation) throws Throwable {
                    Object[] args = invocation.getArguments();
                    Long id = (Long) args[0];
                    return em.find(Resource.class, id);
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
        assertThat(tran.getResource().getBalance()).isEqualTo(BigDecimal.ONE.add(BigDecimal.ONE));

        PaginatedListWrapper<Transaction> trans = transactionManager.getTransactions(1, "id", "asc");
        assertThat(trans.getList().size()).isEqualTo(1);
        assertThat(trans.getList().get(0)).isEqualTo(tran);

        transactionManager.deleteTransaction(tran.getId());
        trans = transactionManager.getTransactions(1, "id", "asc");
        assertThat(trans.getList().size()).isEqualTo(0);
    }

}
