package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.Resource;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.TestWebArchiveHelper;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
@Transactional
public class ResourceManagerImplTest {

    @InjectMocks
    private ResourceManagerImpl resourceManager;

    @Mock
    private UserManager userManager;

    @Inject
    private UserManagerHelper testUserManager;

    @Inject
    private CurrencyManager currencyManager;

    @Spy
    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    private User user;
    private Currency cur;

    @Deployment
    public static Archive<?> getDeployment() {
        return TestWebArchiveHelper.getDeployment();
    }

    @Before
    public void createUser() {

        cur = new Currency();
        cur.setCode("CUR");
        cur.setName("CURRENCY");
        cur = currencyManager.saveCurrency(cur);
        User u = new User();
        u.setUserName("john tester");
        u.setDefaultCurrency(cur);
        user = testUserManager.storeUser(u);

        MockitoAnnotations.initMocks(this);
        Mockito.when(userManager.getCurrentUser()).thenReturn(user);

    }

    @After
    public void clearRecords() {
        PaginatedListWrapper<Resource> resources = resourceManager.getUserResources(1, "id", "asc", false);
        for (Resource r : resources.getList()) {
            resourceManager.deleteResource(r.getId());
        }

        User u = em.find(User.class, user.getId());
        em.remove(u);

        currencyManager.deleteCurrency(cur.getId());
    }

    @Ignore
    @Test
    public void testSaveGetDelete() {
        Resource res = new Resource();
        res.setName("my resource");
        res.setBalance(BigDecimal.TEN);
        res.setUser(user);
        res.setCurrency(user.getDefaultCurrency());

        Resource stored = resourceManager.saveResource(res);

        assertThat(stored.getId()).isNotNull();

        PaginatedListWrapper<Resource> resources = resourceManager.getUserResources(1, "id", "asc", false);
        assertThat(resources.getList().size()).isEqualTo(1);
        assertThat(resources.getList().get(0)).isEqualTo(stored);

        resourceManager.deleteResource(stored.getId());
        resources = resourceManager.getUserResources(1, "id", "asc", false);
        assertThat(resources.getList().size()).isEqualTo(0);
    }

    @Ignore
    @Test
    public void testDefaultCurrency() {
        Resource res = new Resource();
        res.setName("my resource");
        res.setBalance(BigDecimal.TEN);
        res.setUser(user);

        res = resourceManager.saveResource(res);
        assertThat(res.getCurrency()).isEqualTo(user.getDefaultCurrency());
    }

    @Ignore
    @Test
    public void testUpdate() {
        Resource res = new Resource();
        res.setName("my resource");
        res.setBalance(BigDecimal.ONE);
        res.setUser(user);

        res = resourceManager.saveResource(res);

        res.setBalance(BigDecimal.TEN);
        res = resourceManager.saveResource(res);

        assertThat(res.getBalance()).isEqualTo(BigDecimal.TEN);
    }

    @Ignore
    @Test
    public void testGetById() {
        
        Resource res = new Resource();
        res.setName("my resource");
        res.setBalance(BigDecimal.ONE);
        res.setUser(user);

        res = resourceManager.saveResource(res);
        
        Resource found = resourceManager.getResource(res.getId());
        assertThat(res).isEqualTo(found);
        
    }

}
