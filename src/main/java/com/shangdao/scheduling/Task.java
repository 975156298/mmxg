package com.shangdao.scheduling;

import com.shangdao.domain.unitBP.UnitBP;
import com.shangdao.domain.unitBP.UnitBPRepository;
import com.shangdao.service.UnitBPService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class Task {
    private static Log logger =LogFactory.getLog(Task.class);
    @Resource
    UnitBPRepository unitBPRepository;
    @Resource
    UnitBPService unitBPService;
    /**
     * 0 0/1 * * * ? 每分钟
     * 0 0 0 * * ?  每天凌晨
     */
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void free() {
//
//        int page = 0;
//        while (true){
//
//            PageRequest pageRequest = PageRequest.of(page,1000);
//            Page<UnitBP> unitBPPage= unitBPRepository.findAllByClose(false,pageRequest);
//            if(unitBPPage.getContent().isEmpty()){
//                break;
//            }
//            page++;
//            unitBPPage.getContent().forEach(unitBP -> {
//                unitBPService.free(unitBP);
//            });
//        }
//    }
}
