package com.emag.controller;

import com.emag.model.dto.orderdto.CreateOrderDTO;
import com.emag.service.OrderService;
import com.emag.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class OrderController extends AbstractController{

    @Autowired
    OrderService orderService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/orders")
    public String createOrder(@RequestBody @Valid CreateOrderDTO dto, HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return orderService.createOrder(dto);
    }

}
