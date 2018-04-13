package com.shangdao.domain.bonusPoint;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BPLogRepository extends BaseRepository<BPLog> {

    Page<BPLog> findAllByUser(User user, Pageable pageable);
    Page<BPLog> findAllByUserAndType(User user, BPLog.Type type ,Pageable pageable);
    Page<BPLog> findAllByType(BPLog.Type type,Pageable pageable);


    BPLog findBPLogByUserAndType(User user,BPLog.Type type);
}
