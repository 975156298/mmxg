package com.shangdao.domain.config;

import com.shangdao.domain.BaseRepository;

public interface ConfigRepository extends BaseRepository<Config> {
    Config findConfigByStrkey(String key);
}
