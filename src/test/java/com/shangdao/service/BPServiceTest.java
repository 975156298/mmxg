package com.shangdao.service;

import com.shangdao.domain.account.Account;
import com.shangdao.domain.account.AccountRepository;
import com.shangdao.domain.bonusPoint.BonusPoint;
import com.shangdao.domain.bonusPoint.BonusPointRepository;
import com.shangdao.domain.reward.Reward;
import com.shangdao.domain.unitBP.UnitBP;
import com.shangdao.domain.unitBP.UnitBPRepository;
import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class BPServiceTest {

    @Autowired
    BPService bpService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BonusPointRepository bonusPointRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    UnitBPRepository unitBPRepository;
    @Test
    public void bpTransfer() {
        User user = userRepository.findUserByMobile("12300000001");
        assertNotNull(user);
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(user,user.getAuthorities());
        BonusPoint bonusPoint = user.getBonusPoint();
        assertNotNull(bonusPoint);
        bonusPoint.setTransaction(BigDecimal.valueOf(100));
        bonusPointRepository.save(bonusPoint);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        User receiveUser = userRepository.findUserByMobile("12300000002");
        BonusPoint receiveBP = receiveUser.getBonusPoint();
        receiveBP.setTransaction(BigDecimal.valueOf(0));
        bonusPointRepository.save(receiveBP);

        bpService.bpTransfer(BigDecimal.valueOf(20),receiveUser.getWalletAddress(),1);

        assertEquals(user.getBonusPoint().getTransaction().compareTo(BigDecimal.valueOf(80)),0);
        System.out.printf(receiveUser.getBonusPoint().getTransaction().toString());
        assertEquals(receiveUser.getBonusPoint().getTransaction().compareTo(BigDecimal.valueOf(19D)),0);
    }
    @Test
    public void bpBuy()
    {
        User user = userRepository.findUserByMobile("12300000001");
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(user,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        Account  account=user.getAccount();
        account.setMoneyBalance(BigDecimal.valueOf(100));
        accountRepository.save(account);
        BonusPoint bonusPoint = user.getBonusPoint();
        bonusPoint.setTransaction(BigDecimal.valueOf(0));
        bonusPoint.setTotal(BigDecimal.valueOf(0));
        bonusPointRepository.save(bonusPoint);
        bpService.bpBuy(BigDecimal.valueOf(10));

        account = user.getAccount();
        assertEquals(0, account.getMoneyBalance().compareTo(BigDecimal.valueOf(90)));
        bonusPoint=user.getBonusPoint();
        assertEquals(0,bonusPoint.getTransaction().compareTo(BigDecimal.valueOf(10)));
        assertEquals(0,bonusPoint.getTotal().compareTo(BigDecimal.valueOf(10)));

    }
    @Test
    public void bpSell()
    {
        User user = userRepository.findUserByMobile("12300000001");
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(user,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        Account  account=user.getAccount();
        account.setMoneyBalance(BigDecimal.valueOf(100));
        accountRepository.save(account);
        BonusPoint bonusPoint = user.getBonusPoint();
        bonusPoint.setTransaction(BigDecimal.valueOf(11));
        bonusPoint.setTotal(BigDecimal.valueOf(11));
        bonusPointRepository.save(bonusPoint);

        bpService.bpSell(BigDecimal.valueOf(10),BigDecimal.valueOf(1));

        account = user.getAccount();
        assertEquals(0,account.getMoneyBalance().compareTo(BigDecimal.valueOf(109.5)));

        bonusPoint = user.getBonusPoint();
        assertEquals(0,bonusPoint.getTotal().compareTo(BigDecimal.valueOf(1)));
        assertEquals(0,bonusPoint.getTransaction().compareTo(BigDecimal.valueOf(1)));
    }
    @Test
    public void toAIEXTRACTION()
    {
        User user = userRepository.findUserByMobile("12300000002");
        TestingAuthenticationToken authenticationToken = new TestingAuthenticationToken(user,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        User inviter = userRepository.findUserByMobile("12300000001");
        BonusPoint inviterBP = inviter.getBonusPoint();
        inviterBP.setTotal(BigDecimal.valueOf(0));
        inviterBP.setTransaction(BigDecimal.valueOf(0));
        bonusPointRepository.save(inviterBP);


        BonusPoint bonusPoint = user.getBonusPoint();
        bonusPoint.setTransaction(BigDecimal.valueOf(100));
        bonusPoint.setTotal(BigDecimal.valueOf(200));
        bonusPoint.setExploitation(BigDecimal.valueOf(100));
        bonusPointRepository.save(bonusPoint);

        bpService.toAIEXTRACTION(BigDecimal.valueOf(100));
        bonusPoint=user.getBonusPoint();
        assertEquals(bonusPoint.getTotal().compareTo(BigDecimal.valueOf(600)),0);
        assertEquals(bonusPoint.getTransaction().compareTo(BigDecimal.valueOf(0)),0);
        assertEquals(bonusPoint.getExploitation().compareTo(BigDecimal.valueOf(600)),0);


        Reward inviterReward=inviter.getReward();
        assertEquals(inviterBP.getTotal().compareTo(BigDecimal.valueOf(10)),0);
        assertEquals(inviterBP.getTransaction().compareTo(BigDecimal.valueOf(10)),0);
        assertEquals(inviterReward.getTotalBonus().compareTo(BigDecimal.valueOf(10)),0);
        assertEquals(inviterReward.getTotalBP().compareTo(BigDecimal.valueOf(100)),0);

        List<UnitBP> unitBPS = unitBPRepository.findAllByBonusPoint(bonusPoint);
        UnitBP unitBP = unitBPS.get(0);
        assertEquals(unitBP.getNumber().compareTo(BigDecimal.valueOf(500)),0);
        assertEquals(unitBP.getLeftOver().compareTo(BigDecimal.valueOf(500)),0);
        assertFalse(unitBP.getClose());

    }
}