package com.shangdao.controller;

import com.shangdao.domain.reward.RewardRepository;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RewardController {
    @Autowired
    RewardRepository rewardRepository;

    @GetMapping("/front/reward/self")
    public ResponseObj rewardSelf() {
        return new ResponseObj(rewardRepository.findRewardByUser(CommonUtil.getPrincipal()));
    }
}
