package com.shangdao.domain.bonusPoint;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "transfer_log")//转账记录
public class TransferLog extends BaseEntity {
    private static final long serialVersionUID = 1205747783062018784L;

    @ManyToOne
    @JoinColumn(name = "from_user")
    private User fromUser;

    @ManyToOne
    @JoinColumn(name = "receive_user")
    private User receiveUser;

    @Column(precision = 10,scale = 4)
    private BigDecimal number;

    private BigDecimal rate;
    private BigDecimal poundage;

    public User getFromUser() {
        return fromUser;
    }

    public void setFromUser(User fromUser) {
        this.fromUser = fromUser;
    }

    public User getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(User receiveUser) {
        this.receiveUser = receiveUser;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }
}
