package com.shangdao.controller;

import com.shangdao.domain.config.Config;
import com.shangdao.domain.config.ConfigLog;
import com.shangdao.domain.config.ConfigLogRepository;
import com.shangdao.domain.config.ConfigRepository;
import com.shangdao.service.ConfigService;
import com.shangdao.util.ConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ConfigLogController {
    @Autowired
    ConfigLogRepository configLogRepository;
    @Autowired
    ConfigRepository configRepository;
    @GetMapping("/admin/bpPriceLog/list")
    public Page<ConfigLog> bpPriceLog(Pageable pageable){
        Config currentPrice = configRepository.findConfigByStrkey(ConfigEnum.CURRENT_PRICE.toString());
        return configLogRepository.findAllByConfig(currentPrice,pageable);
    }
}
