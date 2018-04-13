package com.shangdao.domain.invite;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface InviteRepository extends BaseRepository<Invite> {

    Invite findInviteByInvitee(User invitee);

    Page<Invite> findAllByInviter(User user, Pageable pageable);

    Page<Invite> findAllByParentInviterOrInviter(User parentInviter,User Inviter,Pageable pageable);
}
