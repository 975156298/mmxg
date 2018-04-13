package com.shangdao.domain.account;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountRepository extends BaseRepository<Account> {
    Account findAccountByUser(User user);
    Page<Account> findAllByUser(User user,Pageable pageable);
}
