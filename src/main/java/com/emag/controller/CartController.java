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
    public void addProductToCArt(@RequestBody CartDTO dto, HttpSession session){
           sessionManager.getLoggedUser(session);
           cartService.addProductToCart(dto.getProductId(),dto.getUserId(),session);
    }

    @DeleteMapping("/cart")
    public void removeProduct(@RequestBody CartDTO dto,HttpSession session){
        sessionManager.getLoggedUser(session);
        cartService.removeProductFromCart(dto.getProductId(),dto.getUserId(),session);
    }

    @PutMapping("/cart/edit")
    public void changeQuantity(@RequestBody UpdateCartQuantityDTO dto,HttpSession session){
        sessionManager.getLoggedUser(session);
        if(dto.getQuantity()<1 || dto.getQuantity()>50){
            throw new BadRequestException("wrong quantity for product");
        }
        cartService.changeQuantityOfProduct(dto.getProductId(),dto.getUserId(),dto.getQuantity(),session);
    }

}
