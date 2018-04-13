package com.shangdao.domain.unitBP;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.bonusPoint.BonusPoint;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "unit_bp")
public class UnitBP extends BaseEntity {
    private static final long serialVersionUID = 7904231655227755480L;

    @Enumerated(EnumType.STRING)
    private Type type;
    @Column(precision = 10,scale = 4)
    private BigDecimal number;
    @Column(name = "left_over",precision = 10,scale = 4)
    private BigDecimal leftOver;//剩下的
    @ManyToOne
    @JoinColumn(name = "bonus_point_id",nullable = false)
    private BonusPoint bonusPoint;

    private Boolean close;//开采完关闭

    public Boolean getClose() {
        return close;
    }

    public void setClose(Boolean close) {
        this.close = close;
    }

    public BonusPoint getBonusPoint() {
        return bonusPoint;
    }

    public void setBonusPoint(BonusPoint bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public BigDecimal getNumber() {
        return number;
    }

    public void setNumber(BigDecimal number) {
        this.number = number;
    }

    public BigDecimal getLeftOver() {
        return leftOver;
    }

    public void setLeftOver(BigDecimal leftOver) {
        this.leftOver = leftOver;
    }

    public enum Type{
        PRIVATE_PLACEMENT,//私募
        EXPLOITATION,//开采
    }
}
