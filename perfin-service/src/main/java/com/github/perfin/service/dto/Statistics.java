package com.github.perfin.service.dto;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Map;
@XmlRootElement
public class Statistics implements Serializable {

    /**
     * First day of date range from which statistics are provided.
     */
    private final String startDate;

    /**
     * Last day of date range from which statistics are provided.
     */
    private final String endDate;

    /**
     * A map containing category name as a key and the sum of all positive amounts from user transactions within the
     * given date range as a value. All incomes are converted to user default currency, rounded on two decimal points
     * and stored as strings.
     */
    private final Map<String, String> incomes;

    /**
     * A sum of all positive amounts from user transactions within the given date range, converted to user default
     * currency, rounded on two decimal points and stored as a string.
     */
    private final String totalIncome;

    /**
     * A map containing category name as a key and a sum of all negative amounts from user transactions within the given
     * date range as a value. All expenses are reversed (positive at the end), converted to user default currency,
     * rounded on two decimal points and stored as strings.
     */
    private final Map<String, String> expenses;

    /**
     * A sum of all negative amounts from user transactions within the given date range, reversed (listed as positive
     * number), converted to user default currency, rounded on two decimal points and stored as a string.
     */
    private final String totalExpense;

    /**
     * User default currency code in which all amounts are listed.
     */
    private final String currencyCode;

    public Statistics(String startDate, String endDate, Map<String, String> incomes, String totalIncome,
            Map<String, String> expenses, String totalExpense, String currencyCode) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.incomes = incomes;
        this.totalIncome = totalIncome;
        this.expenses = expenses;
        this.totalExpense = totalExpense;
        this.currencyCode = currencyCode;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Map<String, String> getIncomes() {
        return incomes;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public Map<String, String> getExpenses() {
        return expenses;
    }

    public String getTotalExpense() {
        return totalExpense;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Statistics that = (Statistics) o;

        if (startDate != null ? !startDate.equals(that.startDate) : that.startDate != null) return false;
        if (endDate != null ? !endDate.equals(that.endDate) : that.endDate != null) return false;
        if (incomes != null ? !incomes.equals(that.incomes) : that.incomes != null) return false;
        if (totalIncome != null ? !totalIncome.equals(that.totalIncome) : that.totalIncome != null) return false;
        if (expenses != null ? !expenses.equals(that.expenses) : that.expenses != null) return false;
        if (totalExpense != null ? !totalExpense.equals(that.totalExpense) : that.totalExpense != null) return false;
        return !(currencyCode != null ? !currencyCode.equals(that.currencyCode) : that.currencyCode != null);

    }

    @Override
    public int hashCode() {
        int result = startDate != null ? startDate.hashCode() : 0;
        result = 31 * result + (endDate != null ? endDate.hashCode() : 0);
        result = 31 * result + (incomes != null ? incomes.hashCode() : 0);
        result = 31 * result + (totalIncome != null ? totalIncome.hashCode() : 0);
        result = 31 * result + (expenses != null ? expenses.hashCode() : 0);
        result = 31 * result + (totalExpense != null ? totalExpense.hashCode() : 0);
        result = 31 * result + (currencyCode != null ? currencyCode.hashCode() : 0);
        return result;
    }

}
