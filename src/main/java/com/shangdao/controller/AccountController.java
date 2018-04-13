package com.shangdao.controller;

import com.shangdao.domain.account.Account;
import com.shangdao.domain.account.AccountRepository;
import com.shangdao.domain.bonusPoint.BPLog;
import com.shangdao.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {
    @Autowired
    AccountRepository accountRepository;
    @GetMapping("/front/account/self")
    public Account self(){
        return accountRepository.findAccountByUser(CommonUtil.getPrincipal());
    }

    @GetMapping("/admin/account/list")
    public Page<Account> list(Pageable pageable){
        return  accountRepository.findAll(pageable);
    }
}
