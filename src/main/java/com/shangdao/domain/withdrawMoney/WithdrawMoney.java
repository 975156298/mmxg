package com.shangdao.domain.withdrawMoney;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.account.Account;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "withdraw_money")//提现
public class WithdrawMoney extends BaseEntity {
    private static final long serialVersionUID = -8771046011221038682L;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Account account;
    private Integer  amount;//提现的金额
    private BigDecimal poundage;//手续费率
    private String bankcard;//银行卡号
    private String openBank;//开户行
    @Enumerated(EnumType.STRING)
    private Status status;
    @ManyToOne
    @JoinColumn(name = "perator_user_id")//操作人
    private User perator;
    public enum Status {
        APPLYING,
        SUCCESS,
        FAIL,
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public User getPerator() {
        return perator;
    }

    public void setPerator(User perator) {
        this.perator = perator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public BigDecimal getPoundage() {
        return poundage;
    }

    public void setPoundage(BigDecimal poundage) {
        this.poundage = poundage;
    }

    public String getBankcard() {
        return bankcard;
    }

    public void setBankcard(String bankcard) {
        this.bankcard = bankcard;
    }

    public String getOpenBank() {
        return openBank;
    }

    public void setOpenBank(String openBank) {
        this.openBank = openBank;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
