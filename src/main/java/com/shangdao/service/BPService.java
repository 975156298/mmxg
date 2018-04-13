package com.shangdao.service;

import com.shangdao.SMS.SMS;
import com.shangdao.domain.account.Account;
import com.shangdao.domain.account.AccountLog;
import com.shangdao.domain.account.AccountRepository;
import com.shangdao.domain.bonusPoint.*;
import com.shangdao.domain.config.Config;
import com.shangdao.domain.config.ConfigRepository;
import com.shangdao.domain.invite.Invite;
import com.shangdao.domain.invite.InviteRepository;
import com.shangdao.domain.reward.Reward;
import com.shangdao.domain.reward.RewardRepository;
import com.shangdao.domain.statistic.Statistic;
import com.shangdao.domain.statistic.StatisticRepository;
import com.shangdao.domain.unitBP.UnitBP;
import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ConfigEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class BPService {
    @Autowired
    ConfigRepository configRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BonusPointRepository bonusPointRepository;
    @Autowired
    BPLogRepository bpLogRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    ConfigService configService;
    @Autowired
    RewardService rewardService;
    @Autowired
    InviteRepository inviteRepository;
    @Autowired
    RewardRepository rewardRepository;
    @Autowired
    UnitBPService unitBPService;
    @Autowired
    TransferLogRepository transferLogRepository;
    @Autowired
    StatisticService statisticService;

    /**
     * 冻结积分时，给上级计算提成
     *
     * @param freezeUser 冻结的user
     * @param freezeBP   冻结的积分
     */
    @Transactional
    public void bonus(User freezeUser, BigDecimal freezeBP) {
        Invite invite = inviteRepository.findInviteByInvitee(freezeUser);
        if (invite == null) {
            return;//没有上级
        }
        User uInviter = invite.getInviter(); //第一层提成
        if (uInviter != null) {
            rewardBonus(uInviter, freezeBP, configService.getFirstBonus());
        }

        if (invite.getParentInviter() != null) {//第二层提成
            rewardBonus(uInviter, freezeBP, configService.getSecondBonus());

        }
    }

    /**
     * @param inviter
     * @param freezeBP    冻结的积分
     * @param configBonus 配置的提成
     */
    private void rewardBonus(User inviter, BigDecimal freezeBP, BigDecimal configBonus) {
        Reward reward = rewardService.findRewardByUser(inviter);
        reward.setTotalBP(reward.getTotalBP().add(freezeBP));//总业绩
        BigDecimal bonus = configBonus.multiply(freezeBP);//奖金
        reward.setTotalBonus(reward.getTotalBonus().add(bonus));
        rewardRepository.save(reward);

        BonusPoint bonusPoint = inviter.getBonusPoint();
        bonusPoint.setTransaction(bonusPoint.getTransaction().add(bonus));//把奖金加入交易积分
        bonusPoint.setTotal(bonusPoint.getTotal().add(bonus));//把奖金加入总积分
        bonusPointRepository.save(bonusPoint);
        addBPLog(bonusPoint, bonus, BPLog.Type.OTHER);
    }

    @Transactional
    public void addBPLog(BonusPoint bp, BigDecimal number, BPLog.Type type) {
        BPLog log = new BPLog();
        log.setNumber(number);
        log.setUser(bp.getUser());
        log.setType(type);
        bpLogRepository.save(log);
    }

    @Transactional
    public BonusPoint createByUser(User user) {
        BonusPoint bonusPoint = bonusPointRepository.findBonusPointByUser(user);
        if (bonusPoint != null) {
            return bonusPoint;
        }
        bonusPoint = new BonusPoint();
        bonusPoint.setUser(user);
        BigDecimal zero = BigDecimal.valueOf(0);
        bonusPoint.setTotal(zero);
        bonusPoint.setTransaction(zero);
        bonusPoint.setExploitation(zero);
        bonusPoint.setPrivatePlacement(zero);
        return bonusPointRepository.save(bonusPoint);
    }


    /**
     * @param bpNumber             积分数量
     * @param receiveWalletAddress 收钱的电子钱包地址
     * @param type                 转账类型 大于0交易积分，小于等于0 私募积分
     */
    @Transactional
    public void bpTransfer(BigDecimal bpNumber, String receiveWalletAddress, int type) {
        Assert.notNull(bpNumber, "转账积分不能为空");

        User currentAuth = CommonUtil.getPrincipal();

        User receiveUser = userRepository.findUserByWalletAddress(receiveWalletAddress);
        Assert.notNull(receiveUser, "接受方不存在");


        BonusPoint sendBP = bonusPointRepository.findBonusPointByUser(currentAuth);

        if (type > 0) {

            if (sendBP.getTransaction().compareTo(bpNumber) < 0) {
                throw new RuntimeException("账户积分不足");
            }
        } else {
            if (sendBP.getPrivatePlacement().compareTo(bpNumber) < 0) {
                throw new RuntimeException("账户积分不足");
            }
        }

        sendBP.setTotal(sendBP.getTotal().subtract(bpNumber));

        if (type > 0) {
            sendBP.setTransaction(sendBP.getTransaction().subtract(bpNumber));
        } else {
            sendBP.setPrivatePlacement(sendBP.getPrivatePlacement().subtract(bpNumber));
        }


        BigDecimal configPoundage = configService.getPoundage();//手续费

//        BigDecimal configPoundage=BigDecimal.valueOf(0);//手续费本来是从表里读的，这里写死 艹

        BigDecimal realBP = bpNumber.multiply(BigDecimal.valueOf(1).subtract(configPoundage));//实际到账积分

        BonusPoint receiveBP = receiveUser.getBonusPoint();
        receiveBP.setTotal(receiveBP.getTotal().add(realBP));

        if (type > 0) {
            receiveBP.setTransaction(receiveBP.getTransaction().add(realBP));
        } else {
            receiveBP.setPrivatePlacement(receiveBP.getPrivatePlacement().add(realBP));
        }


        bonusPointRepository.save(sendBP);
        addBPLog(sendBP, bpNumber.negate(), BPLog.Type.TRANSFER);

        bonusPointRepository.save(receiveBP);
        addBPLog(receiveBP, realBP, BPLog.Type.TRANSFER);

        TransferLog transferLog = new TransferLog();
        transferLog.setFromUser(sendBP.getUser());
        transferLog.setNumber(bpNumber);
        transferLog.setReceiveUser(receiveUser);
        transferLog.setRate(configPoundage);
        transferLog.setPoundage(bpNumber.subtract(realBP));
        transferLogRepository.save(transferLog);

        Statistic ttp = statisticService.getTTP();
        ttp.setValue(BigDecimal.valueOf(Double.valueOf(ttp.getValue())).add(transferLog.getPoundage()).toString());
        statisticService.save(ttp);

    }

    /**
     * @param bpNumber 积分数量
     */
    @Transactional
    public void bpBuy(BigDecimal bpNumber) {
        User currentAuth = CommonUtil.getPrincipal();

        BigDecimal currentPrice = configService.getCurrentPrice();//当前价格
        BigDecimal totalPrice = bpNumber.multiply(currentPrice);

        Account account = accountRepository.findAccountByUser(currentAuth);
        if (totalPrice.compareTo(account.getMoneyBalance()) > 0) {
            throw new RuntimeException("账户余额不足");
        }

        BonusPoint buyBP = bonusPointRepository.findBonusPointByUser(currentAuth);


        account.setMoneyBalance(account.getMoneyBalance().subtract(totalPrice));
        accountRepository.save(account);
        buyBP.setTransaction(buyBP.getTransaction().add(bpNumber));
        buyBP.setTotal(buyBP.getTotal().add(bpNumber));
        bonusPointRepository.save(buyBP);

        addBPLog(buyBP, bpNumber, BPLog.Type.BUY);
        accountService.addAccountLog(account, totalPrice.negate(), AccountLog.Type.BUY);
    }

    @Transactional
    public void bpSell(BigDecimal bpNumber, BigDecimal sellPrice) {
        BigDecimal poundage = configService.getPoundage();//手续费
        BigDecimal currentPrice = configService.getCurrentPrice();//当前价格

        if (sellPrice.compareTo(currentPrice) > 0) {
            throw new RuntimeException("卖出价格不能高于当天价格");
        }

        BigDecimal sellTotalPrice = bpNumber.multiply(sellPrice);
        BigDecimal realityMoney = sellTotalPrice.multiply(BigDecimal.valueOf(1).subtract(poundage));//实际到账的钱

        User user = CommonUtil.getPrincipal();
        Account account = accountRepository.findAccountByUser(user);
        account.setMoneyBalance(account.getMoneyBalance().add(realityMoney));
        accountRepository.save(account);
        accountService.addAccountLog(account, realityMoney, AccountLog.Type.SELL);

        BonusPoint bonusPoint = bonusPointRepository.findBonusPointByUser(user);
        if (bpNumber.compareTo(bonusPoint.getTransaction()) > 0) {
            throw new RuntimeException("卖出数量不能高于交易积分");
        }
        bonusPoint.setTotal(bonusPoint.getTotal().subtract(bpNumber));
        bonusPoint.setTransaction(bonusPoint.getTransaction().subtract(bpNumber));
        bonusPointRepository.save(bonusPoint);
        addBPLog(bonusPoint, bpNumber.negate(), BPLog.Type.SELL);
    }

    /**
     * @param exBP 开采积分
     */
    @Transactional
    public void toAIEXTRACTION(BigDecimal exBP) {//转到智能开采
        User user = CommonUtil.getPrincipal();
        BonusPoint bonusPoint = bonusPointRepository.findBonusPointByUser(user);
        if (bonusPoint.getTransaction().compareTo(exBP) < 0) {
            throw new RuntimeException("交易积分不够");
        }

        BigDecimal freezeMultiple = configService.getFreezeMultiple();


        bonusPoint.setTransaction(bonusPoint.getTransaction().subtract(exBP));//交易积分减少 exBP 这么多
        BigDecimal addExp = exBP.multiply(freezeMultiple);/**开采积分乘于倍数**/
        bonusPoint.setExploitation(bonusPoint.getExploitation().add(addExp));
        BigDecimal realAddBP = addExp.subtract(exBP);
        bonusPoint.setTotal(bonusPoint.getTotal().add(realAddBP));
        addBPLog(bonusPoint, addExp, BPLog.Type.TO_AI_EXTRACTION);

        bonus(user, exBP);//计算上级提成

        //放到unitBP里

        unitBPService.create(UnitBP.Type.EXPLOITATION, addExp, bonusPoint);

        bonusPointRepository.save(bonusPoint);

    }

    @Transactional
    public void updatePrivateBP(int userid, BigDecimal privateBPNumber) {
        Optional<User> optionalUser = userRepository.findById(userid);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        User user = optionalUser.get();

        BonusPoint bonusPoint = user.getBonusPoint();


        bonusPoint.setTotal(bonusPoint.getTotal().add(privateBPNumber));
        bonusPoint.setPrivatePlacement(bonusPoint.getPrivatePlacement().add(privateBPNumber));
        bonusPointRepository.save(bonusPoint);
        BPLog.Type type = privateBPNumber.compareTo(BigDecimal.valueOf(0)) < 0 ? BPLog.Type.PRIVATE_SUB : BPLog.Type.PRIVATE_ADD;
        addBPLog(bonusPoint, privateBPNumber, type);

        //放到unitBP里

        unitBPService.create(UnitBP.Type.PRIVATE_PLACEMENT, privateBPNumber, bonusPoint);
    }
}
