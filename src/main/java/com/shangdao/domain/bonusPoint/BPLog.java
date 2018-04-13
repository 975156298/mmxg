package com.shangdao.domain.bonusPoint;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bp_log")
public class BPLog extends BaseEntity {
    private static final long serialVersionUID = 6139338593432024736L;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(precision = 10,scale = 4)
    private BigDecimal number;//负数- 正数+
    @Enumerated(EnumType.STRING)
    private Type type;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type{
        BUY,//买入
        SELL,//卖出
        TRANSFER,//转账
        RECORD,//入账
        BALANCE,//结算
        AI_EXTRACTION,//智能开采
        TO_AI_EXTRACTION,//转智能开采
        OTHER,//其它
        PRIVATE_ADD,//私募积分新增
        PRIVATE_SUB,//私募积分减去的
    }
}
