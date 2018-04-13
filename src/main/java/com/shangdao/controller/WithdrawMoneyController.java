package com.shangdao.controller;

import com.shangdao.domain.refill.Refill;
import com.shangdao.domain.withdrawMoney.WithdrawMoney;
import com.shangdao.domain.withdrawMoney.WithdrawMoneyRepository;
import com.shangdao.service.UserService;
import com.shangdao.service.WithdrawMoneyService;
import com.shangdao.util.CommonUtil;
import com.shangdao.util.ResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Map;

@RestController
public class WithdrawMoneyController {

    @Autowired
    WithdrawMoneyRepository withdrawMoneyRepository;
    @Autowired
    WithdrawMoneyService withdrawMoneyService;
    @Autowired
    UserService userService;
    @PostMapping("/front/withdrawMoney/create")//提现
    public ResponseObj create(@RequestParam Map<String ,String> reqMap) {

        userService.verifySecondPassword(reqMap.get("secondpwd"),CommonUtil.getPrincipal());
        WithdrawMoney withdrawMoney = new WithdrawMoney();
        withdrawMoney.setAmount(CommonUtil.strConvertToInteger(reqMap.get("amount")));
        withdrawMoney.setBankcard(reqMap.get("bankcard"));
//        withdrawMoney.setPoundage(CommonUtil.strConvertToBigDecimal(reqMap.get("poundage")));
        withdrawMoney.setOpenBank(reqMap.get("openbank"));

        withdrawMoneyService.create(withdrawMoney,CommonUtil.getPrincipal());
        return new ResponseObj();
    }

    @GetMapping("/front/withdrawMoney/self")
    public Page<WithdrawMoney> refillSelf(HttpServletRequest request, Pageable pageable) {

        WithdrawMoney.Status status = WithdrawMoney.Status.valueOf(request.getParameter("status"));

        return withdrawMoneyRepository.findAllByStatusAndUser(status, CommonUtil.getPrincipal(), pageable);
    }

    @GetMapping("/admin/withdrawMoney/list")
    public Page<WithdrawMoney> bpList(HttpServletRequest request, Pageable pageable,@RequestParam(name = "status",required = false) WithdrawMoney.Status status) {

        if(status==null){
            return withdrawMoneyRepository.findAll(pageable);
        }
        return withdrawMoneyRepository.findAllByStatus(status, pageable);
    }


    @PostMapping("/admin/withdrawMoney/success")//充值通过
    public ResponseObj success(HttpServletRequest request) {

        int id = CommonUtil.strConvertToInteger(request.getParameter("id"));
        withdrawMoneyService.success(id);
        return new ResponseObj();
    }
    @PostMapping("/admin/withdrawMoney/fail")//拒绝了
    public ResponseObj fail(HttpServletRequest request) {

        int id = CommonUtil.strConvertToInteger(request.getParameter("id"));
        withdrawMoneyService.fail(id);
        return new ResponseObj();
    }
}
