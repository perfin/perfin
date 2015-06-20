package com.github.perfin.service.dto;

import java.io.Serializable;
import java.util.Map;

public class ResourceBalance implements Serializable {

    /**
     * A map containing resource name as a key and current resource balance as a value. All balances are converted to
     * user default currency, rounded on two decimal points and stored as strings.
     */
    private final Map<String, String> balances;

    /**
     * A sum of all current resource balances converted to user default currency, rounded on two decimal points and
     * stored as a string.
     */
    private final String totalAmount;

    /**
     * User default currency code in which all amounts are listed.
     */
    private final String currencyCode;

    public ResourceBalance(Map<String, String> balances, String totalAmount, String currencyCode) {
        this.balances = balances;
        this.totalAmount = totalAmount;
        this.currencyCode = currencyCode;
    }

    public Map<String, String> getBalances() {
        return balances;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ResourceBalance that = (ResourceBalance) o;

        if (balances != null ? !balances.equals(that.balances) : that.balances != null) return false;
        if (totalAmount != null ? !totalAmount.equals(that.totalAmount) : that.totalAmount != null) return false;
        return !(currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null);

    }

    @Override
    public int hashCode() {
        int result = balances != null ? balances.hashCode() : 0;
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        return result;
    }

}
