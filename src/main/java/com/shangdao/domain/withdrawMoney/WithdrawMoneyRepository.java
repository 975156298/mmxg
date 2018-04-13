package com.shangdao.domain.withdrawMoney;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WithdrawMoneyRepository extends BaseRepository<WithdrawMoney> {
    Page<WithdrawMoney> findAllByStatusAndUser(WithdrawMoney.Status status, User user, Pageable pageable);
    Page<WithdrawMoney> findAllByStatus(WithdrawMoney.Status status, Pageable pageable);
}
