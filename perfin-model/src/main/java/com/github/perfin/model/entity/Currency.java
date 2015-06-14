package com.github.perfin.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

@Entity
@NamedQuery(name="getAllCurrencies", query="SELECT * FROM Currency c")
public class Currency implements Serializable {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    @Length(min=3, max=3, message="Currency Code must be of length: 3")
    private String code;

    @Column
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
