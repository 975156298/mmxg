package com.shangdao.domain.unitBP;

import com.shangdao.domain.BaseRepository;
import com.shangdao.domain.bonusPoint.BonusPoint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitBPRepository extends BaseRepository<UnitBP> {
    Page<UnitBP> findAllByClose(boolean close,Pageable pageable);
    List<UnitBP> findAllByBonusPoint(BonusPoint bonusPoint);
}
