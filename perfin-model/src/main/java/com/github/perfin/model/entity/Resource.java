package com.github.perfin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
public class Resource implements Serializable {

    
    private static final long serialVersionUID = -4452987200669023961L;

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false)
    @Length(min=1, message="Name of resource can't be empty")
    private String name;

    @ManyToOne(optional=false)
    @NotNull(message="Resource mus have specified currency")
    private Currency currency;

    @Column(nullable = false)
    @NotNull(message = "Resource can't have null initial balance")
    @Min(value=0, message="Resource can't have negative initial balance")
    private BigDecimal initialBalance;

    @Column(nullable = false)
    @NotNull(message = "Resource can't have null current balance")
    private BigDecimal currentBalance;

    @ManyToOne(optional=false)
    @NotNull(message = "Resource must belong to some user")
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public void setInitialBalance(BigDecimal initialBalance) {
        this.initialBalance = initialBalance;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Resource [id=" + id + ", name=" + name + ", currency="
                + currency + ", initialBalance=" + initialBalance
                + ", currentBalance=" + currentBalance + ", user=" + user + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((currency == null) ? 0 : currency.hashCode());
        result = prime * result
                + ((currentBalance == null) ? 0 : currentBalance.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((initialBalance == null) ? 0 : initialBalance.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((user == null) ? 0 : user.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Resource other = (Resource) obj;
        if (currency == null) {
            if (other.currency != null)
                return false;
        } else if (!currency.equals(other.currency))
            return false;
        if (currentBalance == null) {
            if (other.currentBalance != null)
                return false;
        } else if (!currentBalance.equals(other.currentBalance))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (initialBalance == null) {
            if (other.initialBalance != null)
                return false;
        } else if (!initialBalance.equals(other.initialBalance))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }
    
    

}
