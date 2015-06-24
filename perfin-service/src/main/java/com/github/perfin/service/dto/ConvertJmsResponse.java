package com.github.perfin.service.dto;

import com.github.perfin.model.entity.Currency;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConvertJmsResponse implements Serializable{

    private BigDecimal amount;

    private Currency originalCurrency;

    private Currency targetCurrency;

    public ConvertJmsResponse() {
    }

    public ConvertJmsResponse(BigDecimal amount, Currency originalCurrency, Currency targetCurrency) {
        this.amount = amount;
        this.originalCurrency = originalCurrency;
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

}
