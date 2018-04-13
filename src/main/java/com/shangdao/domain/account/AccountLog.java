package com.shangdao.domain.account;

import com.shangdao.domain.BaseEntity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "account_log")
public class AccountLog extends BaseEntity {
    private static final long serialVersionUID = 9201528765678149398L;

    @Column(precision = 10,scale = 3)
    private BigDecimal money;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        BUY,//买积分
        SELL,//卖积分
        REFILL,//充值
        WITHDRAW,//提现
    }
}
