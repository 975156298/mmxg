package com.shangdao.service;

import com.shangdao.domain.bonusPoint.BPLog;
import com.shangdao.domain.bonusPoint.BonusPoint;
import com.shangdao.domain.bonusPoint.BonusPointRepository;
import com.shangdao.domain.unitBP.UnitBP;
import com.shangdao.domain.unitBP.UnitBPRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Service
public class UnitBPService {
    @Resource
    UnitBPRepository unitBPRepository;
    @Resource
    BonusPointRepository bonusPointRepository;
    @Resource
    BPService bpService;

    @Transactional
    public UnitBP create(UnitBP.Type type, BigDecimal number, BonusPoint bonusPoint) {
        UnitBP unitBP = new UnitBP();
        unitBP.setBonusPoint(bonusPoint);
        unitBP.setLeftOver(number);
        unitBP.setNumber(number);
        unitBP.setType(type);
        unitBP.setClose(false);

        return unitBPRepository.save(unitBP);
    }

    @Transactional
    public void free(UnitBP unitBP) {
        UnitBP.Type type = unitBP.getType();
        BonusPoint bonusPoint = unitBP.getBonusPoint();
        if(bonusPoint==null){
            return;
        }
        BigDecimal freeBP=BigDecimal.valueOf(0);
        switch (type) {
            case EXPLOITATION:
                freeBP=unitBP.getNumber().multiply(BigDecimal.valueOf(0.001));

                bonusPoint.setExploitation(bonusPoint.getExploitation().subtract(freeBP));

                break;
            case PRIVATE_PLACEMENT:
                freeBP=unitBP.getNumber().multiply(BigDecimal.valueOf(0.01));

                bonusPoint.setPrivatePlacement(bonusPoint.getPrivatePlacement().subtract(freeBP));

                break;
        }
        bonusPoint.setTransaction(bonusPoint.getTransaction().add(freeBP));
        bonusPointRepository.save(bonusPoint);
        bpService.addBPLog(bonusPoint,freeBP,BPLog.Type.AI_EXTRACTION);

        unitBP.setLeftOver(unitBP.getLeftOver().subtract(freeBP));
        if(unitBP.getLeftOver().compareTo(BigDecimal.valueOf(0))<=0){
            unitBP.setClose(true);
        }
        unitBPRepository.save(unitBP);
    }
}
