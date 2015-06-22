package com.github.perfin.service.batch;

import javax.batch.api.chunk.AbstractItemReader;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.perfin.service.rest.ExchangeRatesProvider;

@Named
public class RatesReader extends AbstractItemReader {

    @Inject
    private ExchangeRatesProvider erp;
    
    @Override
    public Object readItem() throws Exception {
        return erp.getStoredRates();
    }

}
