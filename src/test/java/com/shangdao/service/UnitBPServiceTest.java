package com.shangdao.service;

import com.shangdao.domain.bonusPoint.BonusPoint;
import com.shangdao.domain.bonusPoint.BonusPointRepository;
import com.shangdao.domain.unitBP.UnitBP;
import com.shangdao.domain.unitBP.UnitBPRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UnitBPServiceTest {
    @Resource
    UnitBPService unitBPService;
    @Resource
    BonusPointRepository bonusPointRepository;
    @Resource
    UnitBPRepository unitBPRepository;

    @Test
    public void free() {
        BonusPoint bonusPoint = new BonusPoint();
        bonusPoint.setTransaction(BigDecimal.valueOf(100));
        bonusPoint.setPrivatePlacement(BigDecimal.valueOf(100));
        bonusPoint.setExploitation(BigDecimal.valueOf(100));
        bonusPoint.setTotal(BigDecimal.valueOf(300));
        bonusPoint= bonusPointRepository.save(bonusPoint);

        unitBPService.create(UnitBP.Type.PRIVATE_PLACEMENT,BigDecimal.valueOf(100),bonusPoint);//1
        unitBPService.create(UnitBP.Type.EXPLOITATION,BigDecimal.valueOf(100),bonusPoint);//0.1

        List<UnitBP> unitBPS =unitBPRepository.findAllByBonusPoint(bonusPoint);

        unitBPS.forEach(unitBP -> {

            unitBPService.free(unitBP);

        });

        Optional<BonusPoint> optionalBonusPoint = bonusPointRepository.findById(bonusPoint.getId());
        BonusPoint bonusPoint1 = optionalBonusPoint.get();
        assertEquals(bonusPoint1.getExploitation().compareTo(BigDecimal.valueOf(99.9)),0);
        assertEquals(bonusPoint1.getPrivatePlacement().compareTo(BigDecimal.valueOf(99)),0);
        assertEquals(bonusPoint1.getTransaction().compareTo(BigDecimal.valueOf(101.1)),0);


    }
}