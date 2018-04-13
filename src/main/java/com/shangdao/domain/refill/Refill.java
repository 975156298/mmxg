package com.shangdao.domain.refill;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.bankcard.Bankcard;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "refill")
public class Refill extends BaseEntity {
    private static final long serialVersionUID = -2012059148952950699L;
    @ManyToOne
    @JoinColumn(name = "apply_user_id")
    private User user;
    @Column(name = "apply_money",precision = 10,scale = 3)//充值金额
    private BigDecimal applyMoney;

    @ManyToOne
    @JoinColumn(name = "perator_user_id")//操作人
    private User perator;
    private String voucher;//凭证

    private String remark;//备注

    @Enumerated(EnumType.STRING)
    private Status status;
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status {
        APPLYING,
        SUCCESS,
        FAIL,
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getApplyMoney() {
        return applyMoney;
    }

    public void setApplyMoney(BigDecimal applyMoney) {
        this.applyMoney = applyMoney;
    }

    public User getPerator() {
        return perator;
    }

    public void setPerator(User perator) {
        this.perator = perator;
    }
}
