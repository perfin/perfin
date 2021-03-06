package com.github.perfin.service.impl;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Currency;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

@RunWith(Arquillian.class)
@Transactional
public class CategoryManagerImplTest {

    @InjectMocks
    private CategoryManagerImpl categoryManager = new CategoryManagerImpl();

    @Mock
    private UserManager userManager;

    @Spy
    @PersistenceContext(unitName = "primary")
    private EntityManager em;

    @Inject
    private CurrencyManager currencyManager;

    @Inject
    private UserManagerHelper testUserManager;

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
        PaginatedListWrapper<Category> categories = categoryManager.getCategories(1, "id", "asc", false);
        for (Category c : categories.getList()) {
            categoryManager.deleteCategory(c.getId());
        }

        User u = em.find(User.class, user.getId());
        em.remove(u);

        currencyManager.deleteCurrency(cur.getId());
    }

    @Ignore
    @Test
    public void testSaveGetDelete() {
        Category unstored = new Category();
        unstored.setName("MyCategory");
        unstored.setUser(user);

        Category johnCategory = categoryManager.saveCategory(unstored);
        assertThat(johnCategory.getId()).isNotNull();

        unstored = new Category();
        unstored.setName("SecondCategory");
        unstored.setUser(user);

        Category johnSecondCategory = categoryManager.saveCategory(unstored);
        assertThat(johnSecondCategory.getId()).isNotNull();

        PaginatedListWrapper<Category> categories = categoryManager.getCategories(1, "id", "asc", false);
        assertThat(categories.getList().size()).isEqualTo(2);
    }

    @Ignore
    @Test
    public void testUpdateCategory() {
        Category category = new Category();
        category.setName("unstored");
        category.setUser(user);
        categoryManager.saveCategory(category);

        Category stored = category;
        stored.setName("stored");
        stored = categoryManager.saveCategory(stored);

        assertThat(category.getId()).isEqualTo(stored.getId());
        assertThat(category.getUser()).isEqualTo(stored.getUser());
        assertThat(category.getName()).isEqualTo("stored");
    }

}
