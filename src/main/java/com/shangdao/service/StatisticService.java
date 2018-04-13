package com.shangdao.service;

import com.shangdao.domain.statistic.Statistic;
import com.shangdao.domain.statistic.StatisticRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

@Service
public class StatisticService {
    final static String ttpKey="TRANSFER_TOTAL_POUNDAGE";
    @Resource
    private StatisticRepository statisticRepository;
    @PostConstruct
    public void init()
    {
        Statistic TTP = statisticRepository.findStatisticByKey(ttpKey);
        if(TTP==null){
            Statistic statistic = new Statistic();
            statistic.setKey(ttpKey);
            statistic.setValue("0");
            statisticRepository.save(statistic);
        }
    }

    public Statistic getTTP()
    {
        Statistic ttp = statisticRepository.findStatisticByKey(ttpKey);
        if(ttp==null){
            throw new RuntimeException("请联系开发人员");
        }
        return  ttp;
    }

    public Statistic save(Statistic statistic){
        return statisticRepository.save(statistic);
    }
}
