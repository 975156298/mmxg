package com.shangdao.service;

import com.shangdao.domain.refill.Refill;
import com.shangdao.domain.user.User;
import com.shangdao.domain.withdrawMoney.WithdrawMoney;
import com.shangdao.domain.withdrawMoney.WithdrawMoneyRepository;
import com.shangdao.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WithdrawMoneyService {
    @Autowired
    WithdrawMoneyRepository withdrawMoneyRepository;
    @Autowired
    ConfigService configService;
    @Autowired
    AccountService accountService;

    private WithdrawMoney updateStatus(WithdrawMoney withdrawMoney, WithdrawMoney.Status status) {

        withdrawMoney.setStatus(status);


        return withdrawMoneyRepository.save(withdrawMoney);
    }

    @Transactional
    public void create(WithdrawMoney withdrawMoney, User user) {
        withdrawMoney.setStatus(WithdrawMoney.Status.APPLYING);
        withdrawMoney.setPoundage(configService.getPoundage());

        withdrawMoney.setUser(user);
        withdrawMoney.setAccount(user.getAccount());
        if (withdrawMoney.getAmount() % 10 != 0) {
            throw new RuntimeException("提现的金额必须是整十的");
        }
        withdrawMoneyRepository.save(withdrawMoney);
    }

    @Transactional
    public void success(int id) {

        WithdrawMoney withdrawMoney  = findById(id);
        withdrawMoney = updateStatus(withdrawMoney, WithdrawMoney.Status.SUCCESS);
        withdrawMoney.setPerator(CommonUtil.getPrincipal());
        withdrawMoneyRepository.save(withdrawMoney);

//        BigDecimal realMoney=BigDecimal.valueOf(withdrawMoney.getAmount()).subtract((BigDecimal.valueOf(1).subtract(withdrawMoney.getPoundage())));
        accountService.subMoney(withdrawMoney.getUser().getAccount(),BigDecimal.valueOf(withdrawMoney.getAmount()));


    }

    @Transactional
    public void fail(int id) {
        updateStatus(findById(id), WithdrawMoney.Status.FAIL);
    }

    public WithdrawMoney findById(int id) {
        Optional<WithdrawMoney> optionalWithdrawMoney = withdrawMoneyRepository.findById(id);
        if (!optionalWithdrawMoney.isPresent()) {


            throw new RuntimeException("这条内容不存在");

        }
        return optionalWithdrawMoney.get();
    }
}
