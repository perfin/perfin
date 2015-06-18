package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;

import javax.inject.Inject;
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
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.api.ResourceManager;
import com.github.perfin.service.impl.ResourceManagerImpl;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@RunWith(Arquillian.class)
@Transactional
public class ResourceManagerImplTest {
    
    @InjectMocks
    private ResourceManagerImpl resourceManager;
    
    @Mock
    private UserManager userManager;
    
    @Inject
    private TestUserManager testUserManager;
    
    @Inject
    private CurrencyManager currencyManager;
    
    @Spy
    @PersistenceContext(unitName="primary")
    private EntityManager em;
    
    private User user;
    
    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, 
                    ResourceManager.class.getPackage(), 
                    ResourceManagerImpl.class.getPackage(), 
                    Resource.class.getPackage(),
                    ExchangeRatesProvider.class.getPackage(),
                    PaginatedListWrapper.class.getPackage()).
                addPackages(true, "org.assertj.core");
        
        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-all").withTransitivity().asFile());
        
        return war;
    }
    
    @Before
    public void createUser() {
        
        if(user == null) {
            Currency cur = currencyManager.createCurrency("CUR", "CURRENCY");
            User u = new User();
            u.setUserName("john tester");
            u.setDefaultCurrency(cur);
            user = testUserManager.storeUser(u);
            
            MockitoAnnotations.initMocks(this);
            Mockito.when(userManager.getCurrentUser()).thenReturn(user);
        }
    }
    
    @Test
    public void testSaveGetDelete() {
        Resource res = new Resource();
        res.setName("my resource");
        res.setInitialBalance(BigDecimal.ONE);
        res.setCurrentBalance(BigDecimal.TEN);
        res.setUser(user);
        res.setCurrency(user.getDefaultCurrency());
        
        Resource stored = resourceManager.saveResource(res);
        
        assertThat(stored.getId()).isNotNull();
        
        PaginatedListWrapper<Resource> resources = resourceManager.getUserResources(1, "id", "asc");
        assertThat(resources.getList().size()).isEqualTo(1);
        assertThat(resources.getList().get(0)).isEqualTo(stored);
        
        resourceManager.deleteResource(stored.getId());
        resources = resourceManager.getUserResources(1, "id", "asc");
        assertThat(resources.getList().size()).isEqualTo(0);
    }
}
