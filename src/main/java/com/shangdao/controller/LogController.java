package com.shangdao.controller;

import com.shangdao.domain.account.AccountLog;
import com.shangdao.domain.account.AccountLogRepository;
import com.shangdao.domain.bonusPoint.BPLog;
import com.shangdao.domain.bonusPoint.BPLogRepository;
import com.shangdao.domain.bonusPoint.TransferLog;
import com.shangdao.domain.bonusPoint.TransferLogRepository;
import com.shangdao.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class LogController {
    @Autowired
    BPLogRepository bpLogRepository;
    @Autowired
    AccountLogRepository accountLogRepository;
    @Autowired
    TransferLogRepository transferLogRepository;

    @GetMapping("/front/bplog/self")
    public Page<BPLog> bpSelf(Pageable pageable ,@RequestParam(name = "type",required = false) BPLog.Type type){
        if(type!=null){
            return bpLogRepository.findAllByUserAndType(CommonUtil.getPrincipal(),type,pageable);
        }
        return bpLogRepository.findAllByUser(CommonUtil.getPrincipal(),pageable);
    }

    @GetMapping("/admin/bplog/list")
    public Page<BPLog> bpList(Pageable pageable,@RequestParam(name = "type",required = false) BPLog.Type type){
        if(type!=null){
            return bpLogRepository.findAllByType(type,pageable);
        }
        return  bpLogRepository.findAll(pageable);
    }
    @GetMapping("/front/accouontlog/self")
    public Page<AccountLog> self(Pageable pageable){
        return accountLogRepository.findAllByAccount(CommonUtil.getPrincipal().getAccount(),pageable);
    }

    @GetMapping("/admin/accouontlog/list")
    public Page<BPLog> list(Pageable pageable){
        return  bpLogRepository.findAll(pageable);
    }
    @GetMapping("/admin/transferlog/list")
    public Page<TransferLog> transferLogsList(Pageable pageable){
        return transferLogRepository.findAll(pageable);
    }
}
