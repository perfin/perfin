package com.github.perfin.model.entity;

import javax.persistence.*;

import java.io.Serializable;

@Entity
@NamedQuery(name = "getUserByUserName", query = "SELECT u FROM User u where u.userName = :userName")
public class User implements Serializable {

    private static final long serialVersionUID = 6769647195003253595L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String userName;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "currency", nullable = false)
    private Currency defaultCurrency;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((defaultCurrency == null) ? 0 : defaultCurrency.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result
                + ((userName == null) ? 0 : userName.hashCode());
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
        User other = (User) obj;
        if (defaultCurrency == null) {
            if (other.defaultCurrency != null)
                return false;
        } else if (!defaultCurrency.equals(other.defaultCurrency))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (userName == null) {
            if (other.userName != null)
                return false;
        } else if (!userName.equals(other.userName))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", userName=" + userName + ", defaultCurrency=" + defaultCurrency + "]";
    }

}
