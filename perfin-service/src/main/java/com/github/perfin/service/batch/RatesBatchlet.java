package com.github.perfin.service.batch;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.batch.api.AbstractBatchlet;
import javax.batch.runtime.BatchStatus;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@Named
public class RatesBatchlet extends AbstractBatchlet {
    
    @Inject
    private ExchangeRatesProvider erp;
    
    @Override
    public String process() throws InterruptedException, ExecutionException {
        System.out.println("Running inside a batchlet");
        List<ExchangeRate> rates = erp.getStoredRates();
        
        for(ExchangeRate er : rates) {
            er.setDate(LocalDate.now());
            er.setRatio(erp.getLatestRatio(er.getOrigin().getCode(), er.getTarget().getCode()).get());
            erp.saveRate(er);
        }
        
        return BatchStatus.COMPLETED.toString();
    }
}
