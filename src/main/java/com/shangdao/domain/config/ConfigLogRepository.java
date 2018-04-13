package com.shangdao.domain.config;

import com.shangdao.domain.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConfigLogRepository extends BaseRepository<ConfigLog> {

    Page<ConfigLog> findAllByConfig(Config config, Pageable pageable);
}
