package com.shangdao.domain.statistic;

import com.shangdao.domain.BaseRepository;

public interface StatisticRepository extends BaseRepository<Statistic> {
    Statistic findStatisticByKey(String key);
}
