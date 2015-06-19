package com.github.perfin.service.impl;

import static org.assertj.core.api.Assertions.*;

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
import org.mockito.Spy;

import org.mockito.MockitoAnnotations;

import com.github.perfin.model.entity.Category;
import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.User;
import com.github.perfin.service.api.CategoryManager;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.api.UserManager;
import com.github.perfin.service.dto.PaginatedListWrapper;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@RunWith(Arquillian.class)
@Transactional
public class CategoryManagerImplTest {

    @InjectMocks
	private CategoryManagerImpl categoryManager = new CategoryManagerImpl();
	
	@Mock
	private UserManager userManager;
	
	@Spy
	@PersistenceContext(unitName="primary")
	private EntityManager em;
	
	@Inject 
	private CurrencyManager currencyManager;
	
	@Inject
	private TestUserManager testUserManager;
	
	private User user;
	
	@Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addPackages(true, 
            		CategoryManager.class.getPackage(), 
            		CategoryManagerImpl.class.getPackage(), 
            		Category.class.getPackage(),
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
	        Currency cur = new Currency();
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
	}
    
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
        
        PaginatedListWrapper<Category> categories = categoryManager.getCategories(1, "id", "asc");
        assertThat(categories.getList().size()).isEqualTo(2);
	}
    
}
