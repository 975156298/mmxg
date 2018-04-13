package com.shangdao.domain.bonusPoint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bonus_point")
public class BonusPoint extends BaseEntity {
    private static final long serialVersionUID = -5519338495768997546L;

    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @Column(precision =10 ,scale = 4)
    private BigDecimal total;//
    @Column(precision =10 ,scale = 4)
    private BigDecimal transaction;//交易积分
    @Column(precision =10 ,scale = 4)
    private BigDecimal exploitation;//待开采积分
    @Column(precision =10 ,scale = 4)
    private BigDecimal privatePlacement;//私募积分

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTransaction() {
        return transaction;
    }

    public void setTransaction(BigDecimal transaction) {
        this.transaction = transaction;
    }

    public BigDecimal getExploitation() {
        return exploitation;
    }

    public void setExploitation(BigDecimal exploitation) {
        this.exploitation = exploitation;
    }

    public BigDecimal getPrivatePlacement() {
        return privatePlacement;
    }

    public void setPrivatePlacement(BigDecimal privatePlacement) {
        this.privatePlacement = privatePlacement;
    }
}
