package com.github.perfin.model.entity;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@NamedQuery(name = "selectByUserName", query = "SELECT r FROM Resource r WHERE r.user.userName = :userName")
public class Resource implements Serializable {

    private static final long serialVersionUID = -4452987200669023961L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false)
    @Length(min = 1, message = "Name of resource can't be empty")
    private String name;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency", nullable = false)
    @NotNull(message = "Resource mus have specified currency")
    private Currency currency;

    @Column(nullable = false)
    @NotNull(message = "Resource can't have null balance")
    private BigDecimal balance;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user", nullable = false)
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Resource resource = (Resource) o;

        if (id != null ? !id.equals(resource.id) : resource.id != null) return false;
        if (name != null ? !name.equals(resource.name) : resource.name != null) return false;
        if (currency != null ? !currency.equals(resource.currency) : resource.currency != null) return false;
        if (balance != null ? !balance.equals(resource.balance) : resource.balance != null)
            return false;
        return !(user != null ? !user.equals(resource.user) : resource.user != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (balance != null ? balance.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

}
