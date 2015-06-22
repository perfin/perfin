package com.github.perfin.service.batch;

import java.util.List;

import javax.batch.api.chunk.AbstractItemWriter;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.perfin.model.entity.ExchangeRate;
import com.github.perfin.service.rest.ExchangeRatesProvider;

@Named
public class RatesWriter extends AbstractItemWriter {

    @Inject
    private ExchangeRatesProvider erp;
    
    @Override
    public void writeItems(List<Object> arg0) throws Exception {
        List<ExchangeRate> rates =  (List<ExchangeRate>) arg0.get(0);
        for(ExchangeRate er : rates) {
            erp.saveRate(er);
        }
        
    }

}
