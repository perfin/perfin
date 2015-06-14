package com.github.perfin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQueries(value={
	@NamedQuery(name="getTransactionsByDateRange", query="SELECT * FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate"),
	@NamedQuery(name="getTransactionsByCategory", query="SELECT * FROM Transaction t WHERE t.category =  :category"),
	@NamedQuery(name="getTransactionsByResource", query="SELECT * FROM Transaction t WHERE t.resource = :resource")
})
public class Transaction implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    private Resource resource;

    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String note;

    @Column
    private Transaction associatedTransaction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Transaction getAssociatedTransaction() {
        return associatedTransaction;
    }

    public void setAssociatedTransaction(Transaction associatedTransaction) {
        this.associatedTransaction = associatedTransaction;
    }

}
