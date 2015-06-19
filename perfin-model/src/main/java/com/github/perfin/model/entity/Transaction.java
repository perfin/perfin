package com.github.perfin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQueries(value={
	@NamedQuery(name="getTransactionsByDateRange", query="SELECT t FROM Transaction t WHERE t.date >= :startDate AND t.date <= :endDate"),
	@NamedQuery(name="getTransactionsByCategory", query="SELECT t FROM Transaction t WHERE t.category =  :category"),
	@NamedQuery(name="getTransactionsByResource", query="SELECT t FROM Transaction t WHERE t.resource = :resource")
})
public class Transaction implements Serializable {

    private static final long serialVersionUID = -4799356004050651738L;

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToOne(optional=false)
    @NotNull(message="transaction must belong to resource")
    private Resource resource;

    @ManyToOne(optional=false)
    @NotNull(message="transaction must belong to category")
    private Category category;

    @Column(nullable = false)
    @NotNull(message="amount of transaction can't be null")
    private BigDecimal amount;

    @Column(nullable = false)
    @NotNull(message="date of transaction can't be null")
    private LocalDate date;

    @Column
    private String note;

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

}
