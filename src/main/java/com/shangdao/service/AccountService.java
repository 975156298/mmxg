package com.shangdao.service;

import com.shangdao.domain.account.Account;
import com.shangdao.domain.account.AccountLog;
import com.shangdao.domain.account.AccountLogRepository;
import com.shangdao.domain.account.AccountRepository;
import com.shangdao.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;

@Service
public class AccountService {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountLogRepository accountLogRepository;

    @Transactional
    public Account createByUser(User user) {
        Account account = accountRepository.findAccountByUser(user);
        if (account != null) {
            return account;
        }

        account = new Account();
        account.setUser(user);
        account.setMoneyBalance(BigDecimal.valueOf(0));
        return accountRepository.save(account);

    }

    @Transactional
    public void addAccountLog(Account account,BigDecimal money, AccountLog.Type type) {
        AccountLog log = new AccountLog();
        log.setMoney(money);
        log.setType(type);
        log.setAccount(account);
        accountLogRepository.save(log);
    }
    @Transactional
    public void addMoney(Account account,BigDecimal money){
        account.setMoneyBalance(account.getMoneyBalance().add(money));
        accountRepository.save(account);
        addAccountLog(account,money,AccountLog.Type.REFILL);
    }
    @Transactional
    public void subMoney(Account account,BigDecimal money){
        account.setMoneyBalance(account.getMoneyBalance().subtract(money));

        if(account.getMoneyBalance().compareTo(BigDecimal.valueOf(0))<0){
            throw new RuntimeException("提现金额不足");
        }
        accountRepository.save(account);
        addAccountLog(account,money,AccountLog.Type.WITHDRAW);
    }
}
