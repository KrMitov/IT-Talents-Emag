package com.emag.controller;

import com.emag.model.dto.coupondto.CouponConfirmationDTO;
import com.emag.model.dto.coupondto.CouponDTO;
import com.emag.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class CouponController extends AbstractController{

    @Autowired
    CouponService couponService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/coupons")
    public CouponConfirmationDTO addCoupon(@RequestBody @Valid CouponDTO dto, HttpSession session){
        sessionManager.adminVerification(session);
        return couponService.createCoupon(dto);
    }

}
