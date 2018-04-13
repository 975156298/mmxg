package com.shangdao.controller;

import com.shangdao.domain.refill.Refill;
import com.shangdao.domain.refill.RefillRepository;
import com.shangdao.domain.withdrawMoney.WithdrawMoney;
import com.shangdao.service.RefillService;
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
import java.util.Map;

@RestController
public class RefillController {
    @Autowired
    RefillService refillService;
    @Autowired
    RefillRepository refillRepository;

    @PostMapping("/front/refill/create")//充值
    public ResponseObj create(@RequestParam Map<String ,String > reqMap) {

        Refill refill = new Refill();
        refill.setApplyMoney(CommonUtil.strConvertToBigDecimal(reqMap.get("applyMoney")));
        refill.setRemark(reqMap.get("remark"));//备注
        refill.setVoucher(reqMap.get("voucher"));//凭证

        refillService.create(CommonUtil.getPrincipal(), refill);
        return new ResponseObj();
    }

    @GetMapping("/front/refill/self")
    public Page<Refill> refillSelf(HttpServletRequest request, Pageable pageable) {

        Refill.Status status = Refill.Status.valueOf(request.getParameter("status"));

        return refillRepository.findAllByStatusAndUser(status, CommonUtil.getPrincipal(), pageable);
    }

    @GetMapping("/admin/refill/list")
    public Page<Refill> bpList(HttpServletRequest request, Pageable pageable,@RequestParam(name = "status",required = false) Refill.Status status) {

        if(status==null){
            return refillRepository.findAll(pageable);
        }
        return refillRepository.findAllByStatus(status, pageable);
    }


    @PostMapping("/admin/refill/success")//充值通过
    public ResponseObj success(HttpServletRequest request) {

        int id = CommonUtil.strConvertToInteger(request.getParameter("id"));
        refillService.success(id);
        return new ResponseObj();
    }
    @PostMapping("/admin/refill/fail")//拒绝了
    public ResponseObj fail(HttpServletRequest request) {

        int id = CommonUtil.strConvertToInteger(request.getParameter("id"));
        refillService.fail(id);
        return new ResponseObj();
    }
}
