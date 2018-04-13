package com.shangdao.service;

import com.shangdao.domain.refill.Refill;
import com.shangdao.domain.refill.RefillRepository;
import com.shangdao.domain.user.User;
import com.shangdao.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class RefillService {

    @Autowired
    RefillRepository refillRepository;
    @Autowired
    AccountService accountService;

    public Refill create(User refillUser, Refill refill) {
        refill.setUser(refillUser);
        refill.setStatus(Refill.Status.APPLYING);
        return refillRepository.save(refill);
    }

    private Refill updateStatus(int id, Refill.Status status) {
        Optional<Refill> optionalRefill = refillRepository.findById(id);
        if (!optionalRefill.isPresent()) {
            throw new RuntimeException("这条内容不存在");
        }
        Refill refill=optionalRefill.get();


        refill.setStatus(status);


        return refillRepository.save(refill);
    }

    @Transactional
    public void success(int id){
        Refill refill = updateStatus(id,Refill.Status.SUCCESS);
        BigDecimal applyMoney = refill.getApplyMoney();

        refill.setPerator(CommonUtil.getPrincipal());
        refillRepository.save(refill);
        accountService.addMoney(refill.getUser().getAccount(),applyMoney);
    }
    @Transactional
    public void fail(int id){
        Refill refill = updateStatus(id,Refill.Status.FAIL);
        refill.setPerator(CommonUtil.getPrincipal());
        refillRepository.save(refill);
    }
}
