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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.github.perfin.model.entity.Currency;
import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.api.CurrencyManager;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@RunWith(Arquillian.class)
@Transactional
public class RatesDownloaderTest {
    
    @Inject
    private ExchangeRatesProvider erp;
    
    @Inject
    private CurrencyManager currencyManager;
    
    private Currency eur;
    private Currency czk;
    private ExchangeRate er;
    
    @Deployment
    public static Archive<?> getDeployment() {
        WebArchive war = ShrinkWrap
                .create(WebArchive.class)
                .addAsResource("META-INF/persistence.xml", "META-INF/persistence.xml")
                .addAsResource("META-INF/batch-jobs/ratesDownloadJob.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"))
                .addPackages(true, "com.github.perfin.model.entity")
                .addPackages(true, "com.github.perfin.service.api")
                .addPackages(true, "com.github.perfin.service.impl")
                .addPackages(true, "com.github.perfin.service.batch")
                .addPackages(true, "com.github.perfin.service.dto")
                .addPackages(true, "com.github.perfin.service.rest")
                .addPackages(true, "org.assertj.core");
        
        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.mockito:mockito-all").withTransitivity().asFile());
        
        war.addAsLibraries(Maven.resolver().loadPomFromFile("pom.xml")
                .resolve("org.apache.commons:commons-lang3").withTransitivity().asFile());
        
        return war;
    }
    
    @Before
    public void createRates() {
        eur = new Currency();
        eur.setCode("EUR");
        eur = currencyManager.saveCurrency(eur);
        
        czk = new Currency();
        czk.setCode("CZK");
        czk = currencyManager.saveCurrency(czk);
        
        er = new ExchangeRate();
        er.setDate(LocalDate.of(2014, 10, 6));
        er.setRatio(BigDecimal.ONE);
        er.setOrigin(eur);
        er.setTarget(czk);
        erp.saveRate(er);
    }
    
    @Test
    @Ignore
    public void testBatchJob() throws InterruptedException {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Long executionId = jobOperator.start("ratesDownloadJob", new Properties());
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);

        int timeout = 100; 
        while (timeout > 0 && !jobExecution.getBatchStatus().equals(BatchStatus.COMPLETED)) {
            Thread.sleep(100);
            timeout = timeout - 1;
        }

        assertThat(jobExecution.getBatchStatus()).isEqualTo(BatchStatus.COMPLETED);
        assertThat(er.getDate()).isEqualTo(LocalDate.now());
    }
}
