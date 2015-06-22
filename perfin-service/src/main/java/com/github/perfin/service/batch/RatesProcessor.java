package com.github.perfin.service.batch;

import java.time.LocalDate;
import java.util.List;

import javax.batch.api.chunk.ItemProcessor;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@Named
public class RatesProcessor implements ItemProcessor {
    
    @Inject
    private ExchangeRatesProvider erp;
    
    @Override
    public Object processItem(Object arg0) throws Exception {
        List<ExchangeRate> rates = (List<ExchangeRate>) arg0;
        
        for(ExchangeRate er : rates) {
            er.setDate(LocalDate.now());
            er.setRatio(erp.getLatestRatio(er.getOrigin().getCode(), er.getTarget().getCode()).get());
        }

        return rates;
    }

}
