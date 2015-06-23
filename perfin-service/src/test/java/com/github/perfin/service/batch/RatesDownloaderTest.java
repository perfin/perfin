package com.github.perfin.service.batch;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Properties;

import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.TestWebArchiveHelper;

@RunWith(Arquillian.class)
public class RatesDownloaderTest {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private UserTransaction utx;
    
    private Currency eur;
    private Currency czk;
    private ExchangeRate er;
    
    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = (WebArchive) TestWebArchiveHelper.getDeployment();
        war.addAsResource("META-INF/batch-jobs/ratesDownloadJob.xml");
        war.addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));
        war.addPackages(true, "com.github.perfin.service.batch");
        
        return war;
    }
    
    @Before
    public void init() throws NotSupportedException, SystemException, 
                              SecurityException, IllegalStateException, 
                              RollbackException, HeuristicMixedException, 
                              HeuristicRollbackException {
        utx.begin();
        em.joinTransaction();
        eur = new Currency();
        eur.setCode("EUR");
        em.persist(eur);
        
        czk = new Currency();
        czk.setCode("CZK");
        em.persist(czk);
        
        er = new ExchangeRate();
        er.setDate(LocalDate.of(2014, 10, 6));
        er.setRatio(BigDecimal.ONE);
        er.setOrigin(eur);
        er.setTarget(czk);
        em.persist(er);
        utx.commit();
    }
    
    @Test
    public void testBatchJob() throws InterruptedException, NotSupportedException, SystemException, SecurityException, IllegalStateException, RollbackException, HeuristicMixedException, HeuristicRollbackException {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("ratesDownloadJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        int timeout = 100; 
        while (timeout > 0 && !jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
            Thread.sleep(100);
            timeout = timeout - 1;
        }

        assertThat(jobExecution.getBatchStatus()).isEqualTo(BatchStatus.COMPLETED);
        
        utx.begin();
        em.joinTransaction();
        er = em.find(ExchangeRate.class, er.getId());
        utx.commit();
        
        assertThat(er.getDate()).isEqualTo(LocalDate.now());
    }
}
