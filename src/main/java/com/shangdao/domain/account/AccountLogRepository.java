package com.shangdao.domain.account;

import com.shangdao.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AccountLogRepository extends BaseRepository<AccountLog> {
    Page<AccountLog> findAllByAccount(Account account, Pageable pageable);
}
