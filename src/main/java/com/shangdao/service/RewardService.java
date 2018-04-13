package com.shangdao.service;

import com.shangdao.domain.config.Config;
import com.shangdao.domain.invite.Invite;
import com.shangdao.domain.invite.InviteRepository;
import com.shangdao.domain.reward.Reward;
import com.shangdao.domain.reward.RewardRepository;
import com.shangdao.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class RewardService {

    @Autowired
    InviteRepository inviteRepository;
    @Autowired
    RewardRepository rewardRepository;
    @Autowired
    ConfigService configService;


    public Reward findRewardByUser(User user) {
        Reward reward = rewardRepository.findRewardByUser(user);
        if(reward!=null){
            return reward;
        }

        reward = new Reward();
        reward.setUser(user);
        reward.setTotalBonus(BigDecimal.valueOf(0));
        reward.setTotalBP(BigDecimal.valueOf(0));
        return rewardRepository.save(reward);
    }
}
