package com.github.perfin.service.dto;


import com.github.perfin.model.entity.Currency;

import java.io.Serializable;
import java.math.BigDecimal;

public class ConvertJmsRequest implements Serializable{

    private BigDecimal amount;

    private Currency originalCurrency;

    private Currency targetCurrency;

    public ConvertJmsRequest() {
    }

    public ConvertJmsRequest(BigDecimal amount, Currency originalCurrency, Currency targetCurrency) {
        this.amount = amount;
        this.originalCurrency = originalCurrency;
        this.targetCurrency = targetCurrency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Currency getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(Currency originalCurrency) {
        this.originalCurrency = originalCurrency;
    }

    public Currency getTargetCurrency() {
        return targetCurrency;
    }

    public void setTargetCurrency(Currency targetCurrency) {
        this.targetCurrency = targetCurrency;
    }
}
