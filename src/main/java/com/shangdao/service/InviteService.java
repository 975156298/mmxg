package com.shangdao.service;

import com.shangdao.domain.invite.Invite;
import com.shangdao.domain.invite.InviteRepository;
import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import com.shangdao.util.RegexUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InviteService {
    @Autowired
    InviteRepository inviteRepository;
    @Autowired
    UserRepository userRepository;

    /**
     *
     * @param inviter 邀请人
     * @param invitee 被邀请人
     * @return invite
     */
    @Transactional
    public Invite createInviteBy(User inviter,User invitee){
        Invite invite = new Invite();
        invite.setInviter(inviter);
        invite.setInvitee(invitee);
        if(inviter.getInviter()!=null){
            invite.setParentInviter(inviter.getInviter());
        }
        return inviteRepository.save(invite);
    }


}
