package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.Principal;

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

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;
import com.github.perfin.service.rest.ExchangeRatesProvider;

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
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true,
                        UserManager.class.getPackage(),
                        UserManagerImpl.class.getPackage(),
                        User.class.getPackage(),
                        PaginatedListWrapper.class.getPackage(),
                        ExchangeRatesProvider.class.getPackage()).
                        addPackages(true, "org.assertj.core");

        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-all").withTransitivity().asFile());

        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.apache.commons:commons-lang3").withTransitivity().asFile());

        return war;
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
