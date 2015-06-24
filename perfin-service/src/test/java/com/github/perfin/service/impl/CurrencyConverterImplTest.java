package com.github.perfin.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;

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
import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.TestWebArchiveHelper;
import com.github.perfin.service.rest.ExchangeRatesProvider;

import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Arquillian.class)
@Transactional
public class CurrencyConverterImplTest {

    @InjectMocks
    private CurrencyConverterImpl currencyConverter = new CurrencyConverterImpl();
    
    @Mock
    private ExchangeRatesProvider erp;
    
    @Spy
    @PersistenceContext(name = "primary")
    private EntityManager em;
    
    private Currency origin;
    private Currency target;
    
    @Deployment
    public static Archive<?> getDeployment(){
        return TestWebArchiveHelper.getDeployment();
    }
    
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(erp.getLatestRatio(any(String.class), any(String.class)))
            .thenThrow(new IllegalStateException("Rates provider shouldn't be called, rates are stored localy"));
        
        origin = new Currency();
        origin.setCode("EUR");
        em.persist(origin);
        
        target = new Currency();
        target.setCode("CZK");
        em.persist(target);
        
        ExchangeRate er = new ExchangeRate();
        er.setDate(LocalDate.now());
        er.setOrigin(origin);
        er.setRatio(BigDecimal.valueOf(25.6));
        er.setTarget(target);
        em.persist(er);
        
        er = new ExchangeRate();
        er.setDate(LocalDate.of(2000, 10, 20));
        er.setOrigin(origin);
        er.setRatio(BigDecimal.valueOf(25.6));
        er.setTarget(origin);
        em.persist(er);
    }
    
    @Test
    public void testCaching() {
        BigDecimal ratio = currencyConverter.convert(BigDecimal.valueOf(100), origin, target);
        assertThat(ratio).isGreaterThanOrEqualTo(BigDecimal.valueOf(25.6).multiply(BigDecimal.valueOf(100)));
        
        try{
            currencyConverter.convert(BigDecimal.valueOf(100), target, origin);
            fail("unstored rate");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage()).contains("Rates provider");
        }
        
        try{
            currencyConverter.convert(BigDecimal.valueOf(100), origin, origin);
            fail("old stored rate");
        } catch (IllegalStateException ex) {
            assertThat(ex.getMessage()).contains("Rates provider");
        }
    }
}
