package com.shangdao.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.account.Account;
import com.shangdao.domain.bonusPoint.BonusPoint;
import com.shangdao.domain.reward.Reward;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "user")
@JsonIgnoreProperties(value={"hibernateLazyInitializer","handler","fieldHandler"})
public class User extends BaseEntity implements UserDetails {
    private static final long serialVersionUID = -779233692135392700L;

    private String name;
    private String password;
    @Column(unique = true)
    private String mobile;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id")
    @JsonIgnore
    private User inviter;
    @Column(name = "wallet_address")//电子钱包地址
    private String walletAddress;
    @Column(name = "second_password")//二级密码
    private String secondPassword;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private Account account;

    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
//    @JsonIgnore
    private BonusPoint bonusPoint;
    @OneToOne(mappedBy = "user",fetch = FetchType.LAZY)
    @JsonIgnore
    private Reward reward;
    private String avatar;//头像

    @Column(unique = true)
    private String inviteCode;//邀请码

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setBonusPoint(BonusPoint bonusPoint) {
        this.bonusPoint = bonusPoint;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Reward getReward() {
        return reward;
    }

    public BonusPoint getBonusPoint() {
        return bonusPoint;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Account getAccount() {
        return account;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }

    public String getWalletAddress() {
        return walletAddress;
    }

    public void setWalletAddress(String walletAddress) {
        this.walletAddress = walletAddress;
    }

    public String getSecondPassword() {
        return secondPassword;
    }

    public void setSecondPassword(String secondPassword) {
        this.secondPassword = secondPassword;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum  Type{
        NORMAL,
        ADMIN,
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection=new ArrayList<>();
        collection.add((GrantedAuthority) () -> type.toString());
        return collection;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return mobile;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.status.equals(Status.NORMAL);
    }
    public enum Status{
        NORMAL,//正常
        DISABLE,//禁用
    }
}
