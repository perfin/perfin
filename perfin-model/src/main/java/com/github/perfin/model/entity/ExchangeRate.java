package com.github.perfin.model.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@NamedQuery(name = "getAllRates", query = "SELECT er FROM ExchangeRate er")
public class ExchangeRate implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "origin", nullable = false)
    @NotNull(message = "origin currency can't be null")
    private Currency origin;

    @ManyToOne(targetEntity = Currency.class)
    @JoinColumn(name = "target", nullable = false)
    @NotNull(message = "target currency can't be null")
    private Currency target;

    @Column(nullable = false)
    @NotNull(message = "ratio can't be null")
    private BigDecimal ratio;

    @Column(nullable = false)
    @NotNull(message = "date can't be null")
    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Currency getOrigin() {
        return origin;
    }

    public void setOrigin(Currency origin) {
        this.origin = origin;
    }

    public Currency getTarget() {
        return target;
    }

    public void setTarget(Currency target) {
        this.target = target;
    }

    public BigDecimal getRatio() {
        return ratio;
    }

    public void setRatio(BigDecimal ratio) {
        this.ratio = ratio;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

}
