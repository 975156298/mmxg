package com.shangdao.controller;

import com.shangdao.domain.account.Account;
import com.shangdao.domain.account.AccountRepository;
import com.shangdao.domain.invite.Invite;
import com.shangdao.domain.invite.InviteRepository;
import com.shangdao.service.InviteService;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class InviteController {
    @Autowired
    InviteRepository inviteRepository;
    @Autowired
    InviteService inviteService;

    @GetMapping("/front/invite/self")//我一共邀请了哪些人
    public Page<Invite> self(Pageable pageable) {
        return inviteRepository.findAllByInviter(CommonUtil.getPrincipal(), pageable);
    }

    @GetMapping("/admin/invite/list")
    public Page<Invite> list(Pageable pageable) {
        return inviteRepository.findAll(pageable);
    }

}
