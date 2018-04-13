package com.shangdao.domain.bankcard;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;

@Table(name = "bank")
@Entity
public class Bankcard extends BaseEntity {
    private static final long serialVersionUID = -5884478920442389841L;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private String bankName;//银行名字
    @Column(name = "account_identity")
    private String accountIdentity;//卡号
}
