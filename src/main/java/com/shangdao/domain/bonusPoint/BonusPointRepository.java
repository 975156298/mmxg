package com.shangdao.domain.bonusPoint;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.user.User;

public interface BonusPointRepository extends BaseRepository<BonusPoint> {
    BonusPoint findBonusPointByUser(User user);
}
