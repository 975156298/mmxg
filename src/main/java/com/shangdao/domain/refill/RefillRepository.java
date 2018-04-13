package com.shangdao.domain.refill;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RefillRepository extends BaseRepository<Refill> {
    Page<Refill> findAllByStatusAndUser(Refill.Status status, User user, Pageable pageable);

    Page<Refill> findAllByStatus(Refill.Status status,Pageable pageable);
}
