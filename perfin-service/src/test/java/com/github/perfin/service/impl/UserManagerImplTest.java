package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Principal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.TestWebArchiveHelper;

@RunWith(Arquillian.class)
@Transactional
public class UserManagerImplTest {

    private static final String TESTUSER = "testuser";
    
    @InjectMocks
    private UserManagerImpl userManagerImpl = new UserManagerImpl();
    
    @Spy
    @PersistenceContext(name = "primary")
    private EntityManager em;
    
    @Mock
    private Principal principal;
    
    @Deployment
    public static Archive<?> getDeployment() {
        return TestWebArchiveHelper.getDeployment();
    }
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(principal.getName()).thenReturn(TESTUSER);
        
        Currency cur = new Currency();
        cur.setCode("ABC");
        em.persist(cur);
        
        User user = new User();
        user.setDefaultCurrency(cur);
        user.setUserName(TESTUSER);
        em.persist(user);
    }
        
    @Test
    public void testCurrentAndChange()  {
        User user = userManagerImpl.getCurrentUser();
        assertThat(user.getUserName()).isEqualTo(TESTUSER);
        assertThat(user.getDefaultCurrency().getCode()).isEqualTo("ABC");
        
        Currency newCurrency = new Currency();
        newCurrency.setCode("CBA");
        em.persist(newCurrency);
        
        userManagerImpl.changeDefaultCurrency(newCurrency);
        
        user = userManagerImpl.getCurrentUser();
        assertThat(user.getUserName()).isEqualTo(TESTUSER);
        assertThat(user.getDefaultCurrency().getCode()).isEqualTo("CBA");
    }
}
