package com.shangdao.controller;

import com.shangdao.domain.bonusPoint.BonusPoint;
import com.shangdao.domain.bonusPoint.BonusPointRepository;
import com.shangdao.domain.invite.Invite;
import com.shangdao.domain.invite.InviteRepository;
import com.shangdao.domain.reward.RewardRepository;
import com.shangdao.domain.user.User;
import com.shangdao.domain.user.UserRepository;
import com.shangdao.service.BPService;
import com.shangdao.service.UserService;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class BPController {
    @Autowired
    BPService bpService;
    @Autowired
    BonusPointRepository bonusPointRepository;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RewardRepository rewardRepository;
    @Autowired
    InviteRepository inviteRepository;

    @PostMapping("/front/bp/transfer")//积分转账
    public ResponseObj bpTransfer(HttpServletRequest request, @RequestParam(name = "type") int type) {
        Double bpNumber = Double.valueOf(request.getParameter("bpNumber"));//转账的积分

        //验证二级密码
        userService.verifySecondPassword(request.getParameter("secondPWD"), CommonUtil.getPrincipal());

        bpService.bpTransfer(BigDecimal.valueOf(bpNumber), request.getParameter("receiveWalletAddress"),type);
        return new ResponseObj();
    }

    @PostMapping("/front/bp/buy")//买入积分
    public ResponseObj bpBuy(HttpServletRequest request) {

        String bpNumber = request.getParameter("bpNumber");

        BigDecimal number = CommonUtil.strConvertToBigDecimal(bpNumber);
        bpService.bpBuy(number);
        return new ResponseObj();
    }

    @PostMapping("/front/bp/sell")//卖出积分
    public ResponseObj bpSell(HttpServletRequest request) {
        userService.verifySecondPassword(request.getParameter("secondPWD"), CommonUtil.getPrincipal());
        BigDecimal bpNumber = CommonUtil.strConvertToBigDecimal(request.getParameter("bmNumber"));
        BigDecimal sellPrice = CommonUtil.strConvertToBigDecimal(request.getParameter("sellPrice"));
        bpService.bpSell(bpNumber, sellPrice);
        return new ResponseObj();
    }

    @GetMapping("/front/bp/selfInfo")//自己积分详情
    public ResponseObj selfInfo() {
        User user = CommonUtil.getPrincipal();

        return new ResponseObj(bonusPointRepository.findBonusPointByUser(user));
    }

    @PostMapping("/front/bp/ToAIEXTRACTION")//智能开采
    public ResponseObj ToAIEXTRACTION(HttpServletRequest request) {
        BigDecimal bpNumber = CommonUtil.strConvertToBigDecimal(request.getParameter("bmNumber"));
        bpService.toAIEXTRACTION(bpNumber);
        return new ResponseObj();
    }

    @PostMapping("/admin/bp/updatePrivateBP")//添加私募积分
    public ResponseObj updatePrivateBP(HttpServletRequest request) {
        //可以为正负
        BigDecimal privateBPNumber = CommonUtil.strConvertToBigDecimal(request.getParameter("privateBPNumber"));//私募积分
        int uid = CommonUtil.strConvertToInteger(request.getParameter("uid"));//id
        bpService.updatePrivateBP(uid,privateBPNumber);
        return new ResponseObj();
    }

    @GetMapping("/admin/bp/bpInfoByUser")//查询某个用户的积分
    public ResponseObj bpInfoByUser(HttpServletRequest request) {
        Integer user_id = CommonUtil.strConvertToInteger(request.getParameter("userid"));
        Optional<User> optionalUser = userRepository.findById(user_id);
        if (!optionalUser.isPresent()) {
            throw new RuntimeException("用户不存在");
        }
        return new ResponseObj(bonusPointRepository.findBonusPointByUser(optionalUser.get()));
    }
    @GetMapping("/front/bp/friend")//朋友积分
    public List<Map> friend(Pageable pageable){
//       Page<Map> page =  rewardRepository.findAllFriend(CommonUtil.getPrincipal().getId(),pageable);
        Page<Invite> invitePage =inviteRepository.findAllByParentInviterOrInviter(CommonUtil.getPrincipal(),CommonUtil.getPrincipal(),pageable);
        List<Map> mapList = new ArrayList<>();
        invitePage.getContent().forEach(invite -> {
//            rewardRepository.findBy(invite.getInviter());
            mapList.add(rewardRepository.getUserMap(invite.getInvitee()));
        });
       return mapList;
    }

}
