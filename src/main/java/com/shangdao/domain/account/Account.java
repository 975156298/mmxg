package com.shangdao.domain.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account")
public class Account extends BaseEntity {
    private static final long serialVersionUID = -2686610187166699718L;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;
    @Column(name = "mooney_balance",precision = 10,scale = 3)
    private BigDecimal moneyBalance;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getMoneyBalance() {
        return moneyBalance;
    }

    public void setMoneyBalance(BigDecimal moneyBalance) {
        this.moneyBalance = moneyBalance;
    }
}
