//package com.shangdao.service;
//
//import com.shangdao.domain.invite.InviteCode;
//import com.shangdao.domain.invite.InviteCodeRepository;
//import org.apache.commons.lang3.RandomStringUtils;
//import org.apache.commons.lang3.RandomUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class InviteCodeService {
//    @Autowired
//    InviteCodeRepository inviteCodeRepository;
//
//    private String generateCode() {
//        return RandomStringUtils.random(15, "ABCDEFGHIJKLMNOPQESTUVWXYZ0123456789");
//    }
//
//    public List<InviteCode> createByNumber(int num) {
//        List<InviteCode> list = new ArrayList<>();
//        for (int i = 0; i < num; i++) {
//            InviteCode inviteCode = new InviteCode();
//            inviteCode.setStatus(InviteCode.Status.NORMAL);
//            inviteCode.setCode(generateCode());
//            list.add(inviteCode);
//        }
//        return inviteCodeRepository.saveAll(list);
//    }
//
//    public InviteCode useCode(String code) {
//        InviteCode inviteCode = inviteCodeRepository.findInviteCodeByCodeAndStatus(code, InviteCode.Status.NORMAL);
//        if (inviteCode == null) {
//            throw new RuntimeException("邀请码不存在");
//        }
//        if (inviteCode.getStatus().equals(InviteCode.Status.INVALID)) {
//            throw new RuntimeException("邀请码已使用过");
//        }
//        inviteCode.setStatus(InviteCode.Status.INVALID);
//        return inviteCodeRepository.save(inviteCode);
//
//    }
//}
