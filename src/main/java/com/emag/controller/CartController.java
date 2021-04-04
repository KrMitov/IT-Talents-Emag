package com.emag.controller;

import com.emag.model.dto.cartdto.CartDTO;
import com.emag.model.dto.cartdto.UpdateCartQuantityDTO;
import com.emag.model.dto.produtcdto.ProductsFromCartForUserDTO;
import com.emag.model.dto.userdto.UserCartDTO;
import com.emag.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
public class CartController extends AbstractController{

    @Autowired
    CartService cartService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/cart")
    public UserCartDTO addProductToCArt(@RequestBody @Valid CartDTO dto, HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.addProductToCart(dto.getProductId(),dto.getUserId());
    }

    @DeleteMapping("/cart")
    public UserCartDTO removeProduct(@RequestBody @Valid CartDTO dto,HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.removeProductFromCart(dto.getProductId(),dto.getUserId());
    }

    @PutMapping("/cart/edit")
    public UserCartDTO changeQuantity(@RequestBody @Valid UpdateCartQuantityDTO dto,HttpSession session){
        sessionManager.userVerification(session,dto.getUserId());
        return cartService.changeQuantityOfProduct(dto.getProductId(),dto.getUserId(),dto.getQuantity());
    }

}
