package com.shangdao.domain.user;

import com.shangdao.domain.BaseEntity;
import com.shangdao.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository extends BaseRepository<User> {
    User findUserByMobile(String mobile);
    Page<User> findAllByMobile(String mobile, Pageable pageable);
    User findUserByWalletAddress(String walletAddress);
    User findUserByInviteCode(String inviteCode);
}
