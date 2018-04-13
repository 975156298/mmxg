package com.shangdao.controller;

import com.shangdao.domain.statistic.Statistic;
import com.shangdao.domain.statistic.StatisticRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class StatisticController {
    @Resource
    StatisticRepository statisticRepository;
    @GetMapping("/admin/statisticAll")
    public List<Statistic> all(){
        return statisticRepository.findAll();
    }
}
