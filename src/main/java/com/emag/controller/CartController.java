package com.emag.controller;


import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.cartdto.CartDTO;
import com.emag.model.dto.cartdto.UpdateCartQuantityDTO;
import com.emag.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
public class CartController extends AbstractController{

    @Autowired
    CartService cartService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/cart")
    public String addProductToCArt(@RequestBody CartDTO dto, HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.addProductToCart(dto.getProductId(),dto.getUserId());
    }

    @DeleteMapping("/cart")
    public String removeProduct(@RequestBody CartDTO dto,HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.removeProductFromCart(dto.getProductId(),dto.getUserId());
    }

    @PutMapping("/cart/edit")
    public String changeQuantity(@RequestBody UpdateCartQuantityDTO dto,HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.changeQuantityOfProduct(dto.getProductId(),dto.getUserId(),dto.getQuantity());
    }

}
