package com.shangdao.domain.invite;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.user.User;

import javax.persistence.*;

@Entity
@Table(name = "invite")
public class Invite extends BaseEntity {
    private static final long serialVersionUID = 5557070572294557430L;

    @ManyToOne
    @JoinColumn(name = "parent_inviter_id")//邀请人的邀请人
    private User parentInviter;
    @ManyToOne
    @JoinColumn(name = "inviter_id")//邀请人
    private User inviter;
    @OneToOne
    @JoinColumn(name = "invitee_id",unique = true)
    private User invitee;//被邀请人

    public User getInvitee() {
        return invitee;
    }

    public void setInvitee(User invitee) {
        this.invitee = invitee;
    }

    public User getParentInviter() {
        return parentInviter;
    }

    public void setParentInviter(User parentInviter) {
        this.parentInviter = parentInviter;
    }

    public User getInviter() {
        return inviter;
    }

    public void setInviter(User inviter) {
        this.inviter = inviter;
    }
}
