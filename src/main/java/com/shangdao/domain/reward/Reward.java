package com.shangdao.domain.reward;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "reward")
public class Reward extends BaseEntity {
    private static final long serialVersionUID = -4967318604657891460L;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "total_bp",precision = 10,scale = 6)
    private BigDecimal totalBP;//总业绩积分
    @Column(name = "total_bonus",precision = 10,scale = 6)
    private BigDecimal totalBonus;//总奖金

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getTotalBP() {
        return totalBP;
    }

    public void setTotalBP(BigDecimal totalBP) {
        this.totalBP = totalBP;
    }

    public BigDecimal getTotalBonus() {
        return totalBonus;
    }

    public void setTotalBonus(BigDecimal totalBonus) {
        this.totalBonus = totalBonus;
    }
}
